function AudioWorks(config) {
  	this.recorder;
  	this.mediaStream;
	this.audio_context = new AudioContext();
	this.audio_context_offline;
	this.analyser;
	this.scriptProcessorNode;
	this.input;
	this.bufferSource;
	this.audioData;
	this.processing = 0;
	this.callback = [];
	this.canvasPos = 0;
	this.audioProperties;
	this.filterChain = [];
	this.customDraw;
	
	//standard config
	//fftSize: values per sample, double height of graph, array is half as big
	//bufferSize: samples per pixel, width of graph
	this.config = {
		"numberOfInputChannels": 2,
		"numberOfOutputChannels": 1,
		"fftSize": 1024,
		"bufferSize": 2048,
		"smoothingTimeConstant": 0.3,
		"outputFilename": "output.wav",
		"canvas": null,
		"diagramType": "spectogram",
		"canvasMargin": 100,
		"spectrogramColour": [ '#000000', '#00aeff', '#f4fcff', '#ffffff' ],
		"waveformColour": "rgb(17, 27, 34)",
		"waveformRMSColour": "#17514b",
		"diagramWidthScale": 1,
		"diagramHeightScale": 1,
		"diagramIntensity": 1
	};

	this.setConfig(config);
	
	if (typeof(this.config.canvas) == "string") {
		this.config.canvas = document.querySelector(this.config.canvas);
	}
  	
  	this.hot = new chroma.ColorScale({
		colors: this.config.spectrogramColour,
		positions: [0, .25, .75, 1],
		mode: 'rgb',
		limits: [0, 255]
	});
}

AudioWorks.prototype.getConfig = function() {
	return this.config;
};

AudioWorks.prototype.setConfig = function(config) {
	if(typeof(config) == "object") {
		for (var id in config) {
  			this.config[id] = config[id];
		}
	}
};

AudioWorks.prototype.getAudioProperties = function() {	
	return this.audioProperties;
};
 
AudioWorks.prototype.startRecord = function() {
	var self = this;
	this.processing++;
    navigator.mediaDevices.getUserMedia({audio: true}).then(function(stream) {
		self.setUpRecording(stream);
	}).catch(function(err) { self.log("getUserMedia", err.message); });
};

AudioWorks.prototype.stopRecord = function() {
	var self = this;
	
	this.mediaStream.getTracks()[0].stop();
	this.recorder.stop();
	
	if(this.config.canvas != null && typeof(this.config.canvas) == "object") {
		this.analyser.disconnect(this.scriptProcessorNode);
		this.scriptProcessorNode.disconnect(this.audio_context.destination);
		this.input.disconnect(this.analyser)
	}
	
	this.recorder.exportWAV(function(blob) {    	
    	var fileReader = new FileReader();
		fileReader.onload = function() {
			self.audioData = this.result.slice(0);
			self.processing--;
			self.checkCallbackStack();
			self.recorder.clear();
			
			self.audio_context.decodeAudioData(this.result.slice(0), function(buffer) {
				self.setAudioProperties(buffer);
			}, function(err) { self.log("stopRecord", err.message); });
			
 		};
		fileReader.readAsArrayBuffer(blob);
	});
};

AudioWorks.prototype.redrawVisualisation = function() {
	var self = this;
	
	this.audio_context.decodeAudioData(this.audioData.slice(0), function(buffer) {
		self.setAudioProperties(buffer);
		var newBuffer = self.processFilterchain(buffer);
		
		if(self.config.canvas != null && typeof(self.config.canvas) == "object") {
			self.canvasPos = 0;
			var ctx = self.config.canvas.getContext("2d");
			ctx.clearRect(0, 0, self.config.canvas.width, self.config.canvas.height);
			
			self.setUpDrawing(newBuffer);
		}
	}, function(err) { self.log("redrawVisualisation", err.message); });
};

AudioWorks.prototype.downloadAudio = function() {
	var self = this;

	if(this.pushToCallbackStack(function() { self.downloadAudio(); }) == false) {
		this.audio_context.decodeAudioData(this.audioData.slice(0), function(buffer) {
			var newBuffer = self.processFilterchain(buffer);
			var wav = self.audioBufferToWav(newBuffer);
	
			var blob = new window.Blob([ new DataView(wav) ], {
				type: 'audio/wav'
			});

			var url = (window.URL || window.webkitURL).createObjectURL(blob);
			var link = window.document.createElement('a');
			link.href = url;
			link.download = self.config.outputFilename;
			var ev = new MouseEvent('click', {	'view': window,
												'bubbles': true,
												'cancelable': true});
			link.dispatchEvent(ev);
		}, function(err) { self.log("downloadAudio", err.message); });
	}
};

AudioWorks.prototype.exportAudio = function(exportAudioCallback) {
    var self = this;
	
	if(this.pushToCallbackStack(function() { self.exportAudio(exportAudioCallback); }) == false) {
		this.audio_context.decodeAudioData(this.audioData.slice(0), function(buffer) {
			var newBuffer = self.processFilterchain(buffer);
			var wav = self.audioBufferToWav(newBuffer);
	
			var blob = new window.Blob([ new DataView(wav) ], {
				type: 'audio/wav'
			});
			exportAudioCallback(blob);
		}, function(err) { self.log("downloadAudio", err.message); });
	}
};

AudioWorks.prototype.importAudioAsArrayBuffer = function(audioData) {
	this.audioData = audioData.slice(0);
	this.redrawVisualisation();
};

AudioWorks.prototype.visualiseSpectrum = function() {
    this.config.diagramType = "spectogram";
    this.redrawVisualisation();
};

AudioWorks.prototype.visualiseWaveform = function() {
    this.config.diagramType = "waveform";
    this.redrawVisualisation();
};

AudioWorks.prototype.visualiseWaveformRMS = function() {
    this.config.diagramType = "waveformrms";
    this.redrawVisualisation();
};

AudioWorks.prototype.visualiseWaveformHQ = function() {
    this.config.diagramType = "waveformhq";
    this.redrawVisualisation();
};

AudioWorks.prototype.trimAudio = function(start, end) {
	var self = this;
	
    if(this.audioProperties != null) {
    	var sampleStart = start*this.audioProperties.sampleRate;
    	var sampleEnd = end*this.audioProperties.sampleRate;
    	
    	this.filterChain.push( function(ctx, buffer) {return self.trimSample(buffer, sampleStart, sampleEnd); } );
    }
    else {
    	this.log("trimAudio", "no audio properties available, can't trim");
    }
};

AudioWorks.prototype.addCustomFilter = function(func) {
	this.filterChain.push( func );
};

AudioWorks.prototype.clearCustomFilter = function() {
	this.filterChain = [];
};

AudioWorks.prototype.setCustomDrawFunction = function(func) {
	this.customDraw = func;
};


/* helper functions */

AudioWorks.prototype.trimSample = function(buff, start, end) {
    var start = this.getStart(start, buff.length);
    var end = this.getEnd(end, buff.length);
    var channels = buff.numberOfChannels;
    var duration = end-start;

    var newAudioBuffer = this.audio_context.createBuffer(channels, duration, buff.sampleRate);

    for (var channel = 0; channel < channels; channel++) {
		var oldChannel = buff.getChannelData(channel);
		newAudioBuffer.copyToChannel(oldChannel.slice(start, end), channel, 0);
	}

    return newAudioBuffer;
};

AudioWorks.prototype.getStart = function(pos, len) {
    if (pos == null) return 0;
    return pos < 0 ? (len + (pos % len)) : Math.min(len, pos);
};

AudioWorks.prototype.getEnd = function(pos, len) {
    if (pos == null) return len;
    return pos < 0 ? (len + (pos % len)) : Math.min(len, pos);
};

AudioWorks.prototype.processFilterchain = function(buff) {
    var bufferReturn = buff;
    
    for (i = 0; i < this.filterChain.length; i++) { 
		bufferReturn = this.filterChain[i](this, bufferReturn);
	}
	return bufferReturn;
};

AudioWorks.prototype.setUpDrawing = function(buffer) {
	var self = this;
	
	this.audio_context_offline = new OfflineAudioContext(2, buffer.length, buffer.sampleRate);
	
	this.analyser = this.audio_context_offline.createAnalyser();
	this.analyser.smoothingTimeConstant = this.config.smoothingTimeConstant;
	this.analyser.fftSize = this.config.fftSize;

	this.scriptProcessorNode = this.audio_context_offline.createScriptProcessor(this.config.bufferSize, this.config.numberOfInputChannels, this.config.numberOfOutputChannels);
	this.scriptProcessorNode.connect(this.audio_context_offline.destination);
	
	this.bufferSource = this.audio_context_offline.createBufferSource();
	this.bufferSource.connect(this.analyser);
	this.analyser.connect(this.scriptProcessorNode);

	//this.displayBuffer(buffer);
	this.scriptProcessorNode.onaudioprocess = function(e){
		self.drawGraph();
	}
	this.bufferSource.buffer = buffer;
	this.bufferSource.start(0);
	this.audio_context_offline.startRendering();
};

AudioWorks.prototype.drawGraph = function() {
	var freqArray =  new Uint8Array(this.analyser.frequencyBinCount);
	this.analyser.getByteFrequencyData(freqArray);
	
	var timeArray = new Uint8Array(this.analyser.fftSize);
	this.analyser.getByteTimeDomainData(timeArray);
	
	this.checkCanvasWidth();
	
	if(this.config.diagramType == "spectogram") this.drawSpectrogram(freqArray);
	else if(this.config.diagramType == "waveform") this.drawWaveform(timeArray, false);
	else if(this.config.diagramType == "waveformrms") this.drawWaveform(timeArray, true);
	else if(this.config.diagramType == "custom") this.customDraw(this, freqArray, timeArray);
	else this.drawSpectrogram(freqArray);
};

AudioWorks.prototype.setUpRecording = function(stream) {
	var self = this;
	this.canvasPos = 0;
	
	this.input = this.audio_context.createMediaStreamSource(stream);
	this.mediaStream = stream;
	
	if(this.config.canvas != null && typeof(this.config.canvas) == "object") {
		var ctx = this.config.canvas.getContext("2d");
		ctx.clearRect(0, 0, this.config.canvas.width, this.config.canvas.height);
		
		this.analyser = this.audio_context.createAnalyser();
		this.analyser.smoothingTimeConstant = this.config.smoothingTimeConstant;
		this.analyser.fftSize = this.config.fftSize;
	
		this.scriptProcessorNode = this.audio_context.createScriptProcessor(this.config.bufferSize, this.config.numberOfInputChannels, this.config.numberOfOutputChannels);
		this.input.connect(this.analyser);
		this.analyser.connect(this.scriptProcessorNode);
		this.scriptProcessorNode.connect(this.audio_context.destination);

		this.scriptProcessorNode.onaudioprocess = function() {
			self.drawGraph();
		}
	}
	
    this.recorder = new Recorder(this.input);
	this.recorder.record();
};

AudioWorks.prototype.drawSpectrogram = function(array) {
	var ctx = this.config.canvas.getContext("2d");
	var height = this.config.canvas.height;
	var maxHeight = (this.config.fftSize/2);
	var scaleFactorHeight = (height/maxHeight)*this.config.diagramHeightScale;
	var scaleFactorWidth = this.config.diagramWidthScale;
	
	ctx.globalCompositeOperation = 'source-over';
	ctx.globalAlpha = 1.0;

	for (var i = 0; i < array.length; i++) {
		var value = array[i];
		ctx.fillStyle = this.hot.getColor(value*this.config.diagramIntensity).hex();
		ctx.fillRect(Math.round(this.canvasPos*scaleFactorWidth), 
					 Math.round(height-((i+1)*scaleFactorHeight)), 
					 Math.round(scaleFactorWidth || 1), 
					 Math.round(scaleFactorHeight) || 1);
	}
	this.canvasPos++;
	//ctx.setTransform(1, 0, 0, 1, 0, 0);
};


AudioWorks.prototype.drawWaveform = function(array, rms) {
	var ctx = this.config.canvas.getContext("2d");
	var height = this.config.canvas.height;
	var maxHeight = 256;
	var scaleFactorHeight = (height/maxHeight)*this.config.diagramHeightScale;
	var scaleFactorWidth = this.config.diagramWidthScale;
	var offset = Math.round(((maxHeight*this.config.diagramHeightScale)-height)/2);;
	
	ctx.fillStyle = this.config.waveformColour;
	//ctx.globalCompositeOperation = 'lighter';
	ctx.globalCompositeOperation = 'screen';
	ctx.globalAlpha = 0.5*this.config.diagramIntensity;

	var rmsp = 0;
	var rmsn = 0;
	var rmspCount = 0;
	var rmsnCount = 0;
	for (var i = 0; i < array.length; i++) {
	   ctx.fillRect(Math.round(this.canvasPos*scaleFactorWidth),
	   				Math.round(array[i]*scaleFactorHeight)-offset,
	   				Math.round(scaleFactorWidth || 1),
	   				Math.round(scaleFactorHeight) || 1);
	   	//root mean square
	   	if(rms == true) {
			if(array[i] >= 128) {
				rmsp = rmsp+array[i]*array[i];
				rmspCount++;
			}
			if(array[i] <= 128) {
				rmsn = rmsn+array[i]*array[i];
				rmsnCount++;
			}
	   	} 	
	}
	//root mean square
	if(rms == true) {
	   rmsp = Math.sqrt( (1/rmspCount)*rmsp );
		rmsn = Math.sqrt( (1/rmsnCount)*rmsn );
		ctx.fillStyle = this.config.waveformRMSColour;
		ctx.globalCompositeOperation = 'source-over';
		ctx.globalAlpha = 0.5*this.config.diagramIntensity;
	
		ctx.fillRect(Math.round(this.canvasPos*scaleFactorWidth),
					 Math.round(rmsn*scaleFactorHeight)-offset,
					 Math.round(scaleFactorWidth || 1),
					 Math.round((rmsp-rmsn)*scaleFactorHeight) || 1);	
	}

	this.canvasPos++;
};

/* 
	inspired by
	http://stackoverflow.com/questions/22073716/create-a-waveform-of-the-full-track-with-web-audio-api/22103150#22103150
*/
AudioWorks.prototype.drawWaveformHQ = function(buff) {
	var ctx = this.config.canvas.getContext("2d")
	var canvasHeight = 500;
	var canvasWidth = 500;
	var leftChannel = buff.getChannelData(0); // Float32Array describing left channel     
	var lineOpacity = 1;//canvasWidth / leftChannel.length  ;      
	ctx.save();
	ctx.fillStyle = '#222' ;
	ctx.strokeStyle = '#121';
	ctx.globalCompositeOperation = 'lighter';
	ctx.translate(0,canvasHeight / 2);
	ctx.globalAlpha = 1.0 ; // lineOpacity ;
	for (var i=0; i<  leftChannel.length; i++) {
	   var x = Math.floor(i/this.config.bufferSize);
	   var y = leftChannel[i] * canvasHeight / 2 ;
	   ctx.beginPath();
	   ctx.moveTo( x  , 0 );
	   ctx.lineTo( x+1, y );
	   ctx.stroke();
	}
	ctx.restore();
};

AudioWorks.prototype.checkCallbackStack = function() {
	if(this.processing <= 0) {
		for (i = 0; i < this.callback.length; i++) { 
			this.callback[i]();
		}
		this.callback = [];
	}
};

AudioWorks.prototype.pushToCallbackStack = function(funct) {
	if(this.processing > 0) {
		this.callback.push(funct);
		return true;
	}
	else {
		return false;
	}
};

AudioWorks.prototype.checkCanvasWidth = function() {
	var ctx = this.config.canvas.getContext("2d");
	var width = this.config.canvas.width;
	var marginLeft = this.config.canvas.width - this.canvasPos*this.config.diagramWidthScale;
	
	if(marginLeft < this.config.canvasMargin) {
		var inMemCanvas = document.createElement('canvas');
		var inMemCtx = inMemCanvas.getContext('2d');
		inMemCanvas.width = this.config.canvas.width;
		inMemCanvas.height = this.config.canvas.height;
		inMemCtx.drawImage(this.config.canvas, 0, 0);
		this.config.canvas.width = this.config.canvas.width + (this.config.canvasMargin - marginLeft) + 1;
		ctx.drawImage(inMemCanvas, 0, 0);
	}
};

AudioWorks.prototype.setAudioProperties = function(buffer) {
	this.audioProperties = {
		"sampleRate": buffer.sampleRate,
		"length": buffer.length,
		"duration": buffer.duration,
		"numberOfChannels": buffer.numberOfChannels
	}
};

AudioWorks.prototype.log = function(src, error) {
	console.log(src + ": " + error);
};



/* wav functions adapted from recorder.js, some code duplications */

AudioWorks.prototype.audioBufferToWav = function(buffer, opt) {
    opt = opt || {}

    var numChannels = buffer.numberOfChannels
    var sampleRate = buffer.sampleRate
    var format = opt.float32 ? 3 : 1
    var bitDepth = format === 3 ? 32 : 16

    var result
    if (numChannels === 2) {
        result = this.interleave(buffer.getChannelData(0), buffer.getChannelData(1))
    } else {
        result = buffer.getChannelData(0)
    }

    return this.encodeWAV(result, format, sampleRate, numChannels, bitDepth)
};

AudioWorks.prototype.encodeWAV = function(samples, format, sampleRate, numChannels, bitDepth) {
    var bytesPerSample = bitDepth / 8
    var blockAlign = numChannels * bytesPerSample

    var buffer = new ArrayBuffer(44 + samples.length * bytesPerSample)
    var view = new DataView(buffer)

    /* RIFF identifier */
    this.writeString(view, 0, 'RIFF')
        /* RIFF chunk length */
    view.setUint32(4, 36 + samples.length * bytesPerSample, true)
        /* RIFF type */
    this.writeString(view, 8, 'WAVE')
        /* format chunk identifier */
    this.writeString(view, 12, 'fmt ')
        /* format chunk length */
    view.setUint32(16, 16, true)
        /* sample format (raw) */
    view.setUint16(20, format, true)
        /* channel count */
    view.setUint16(22, numChannels, true)
        /* sample rate */
    view.setUint32(24, sampleRate, true)
        /* byte rate (sample rate * block align) */
    view.setUint32(28, sampleRate * blockAlign, true)
        /* block align (channel count * bytes per sample) */
    view.setUint16(32, blockAlign, true)
        /* bits per sample */
    view.setUint16(34, bitDepth, true)
        /* data chunk identifier */
    this.writeString(view, 36, 'data')
        /* data chunk length */
    view.setUint32(40, samples.length * bytesPerSample, true)
    if (format === 1) { // Raw PCM
        this.floatTo16BitPCM(view, 44, samples)
    } else {
        this.writeFloat32(view, 44, samples)
    }

    return buffer
};

AudioWorks.prototype.interleave = function(inputL, inputR) {
    var length = inputL.length + inputR.length
    var result = new Float32Array(length)

    var index = 0
    var inputIndex = 0

    while (index < length) {
        result[index++] = inputL[inputIndex]
        result[index++] = inputR[inputIndex]
        inputIndex++
    }
    return result
};

AudioWorks.prototype.writeFloat32 = function(output, offset, input) {
    for (var i = 0; i < input.length; i++, offset += 4) {
        output.setFloat32(offset, input[i], true)
    }
};

AudioWorks.prototype.floatTo16BitPCM = function(output, offset, input) {
    for (var i = 0; i < input.length; i++, offset += 2) {
        var s = Math.max(-1, Math.min(1, input[i]))
        output.setInt16(offset, s < 0 ? s * 0x8000 : s * 0x7FFF, true)
    }
};

AudioWorks.prototype.writeString = function(view, offset, string) {
    for (var i = 0; i < string.length; i++) {
        view.setUint8(offset + i, string.charCodeAt(i))
    }
},

/* end of wav functions adapted from recorder.js*/



window.URL = window.URL || window.webkitURL;
window.AudioContext = window.AudioContext || window.webkitAudioContext || window.mozAudioContext;

/*if(window.AudioContext){
	console.log("AudioContext not supported.");
}*/
   
var promisifiedOldGUM = function(constraints) {

	// First get ahold of getUserMedia, if present
	var getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia);
	navigator.getUserMedia = getUserMedia;
	// Some browsers just don't implement it - return a rejected promise with an error
	// to keep a consistent interface
	if(!getUserMedia) {
		return Promise.reject(new Error('getUserMedia is not implemented in this browser'));
	}

	// Otherwise, wrap the call to the old navigator.getUserMedia with a Promise
	return new Promise(function(resolve, reject) {
		getUserMedia.call(navigator, constraints, resolve, reject);
	});
		
}

// Older browsers might not implement mediaDevices at all, so we set an empty object first
if(navigator.mediaDevices === undefined) {
	navigator.mediaDevices = {};
}

// Some browsers partially implement mediaDevices. We can't just assign an object
// with getUserMedia as it would overwrite existing properties.
// Here, we will just add the getUserMedia property if it's missing.
if(navigator.mediaDevices.getUserMedia === undefined) {
	navigator.mediaDevices.getUserMedia = promisifiedOldGUM;
}