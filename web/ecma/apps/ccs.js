function CartesianCoordinateSystem(canvasID, xAxisMin, xAxisMax, yAxisMin, yAxisMax, unitsPerMarker, markSize) {
    var canvas = document.getElementById(canvasID);
    welt.init_canvas(canvasID, { width: canvas.width, height: canvas.height, xmin: xAxisMin, xmax: xAxisMax, ymin: yAxisMin, ymax: yAxisMax});
    this.ctx = welt.get_ctx_von(canvasID);
    // conf values
    this.xAxisMin = xAxisMin;
    this.xAxisMax = xAxisMax;
    this.yAxisMin = yAxisMin;
    this.yAxisMax = yAxisMax;
    this.unitsPerMarker = unitsPerMarker;
    this.markSize = markSize;
    this.drawResolution = 20;
    this.lastEqu = function(x) {return 0;};

    // function to draw the axis
    this.drawAxis = function() {
        // draw x and y axis
        welt.save();
        welt.beginPath();
        welt.moveTo(this.xAxisMin, 0);
        welt.lineTo(this.xAxisMax, 0);
        welt.moveTo(0, this.yAxisMin);
        welt.lineTo(0, this.yAxisMax);
        welt.stroke();

        // draw marks for axises
        this.ctx.textAlign = 'center';
        this.ctx.textBaseline = 'top';
        for(var xAxisPos = 0+parseInt(this.unitsPerMarker*100); xAxisPos < xAxisMax; xAxisPos += this.unitsPerMarker*100) {
            welt.moveTo(xAxisPos, -this.markSize/2);
            welt.lineTo(xAxisPos, this.markSize/2);
            welt.stroke();
            welt.fillText(String(xAxisPos), xAxisPos, -this.markSize/2);
        }
        for(var xAxisPos = 0-parseInt(this.unitsPerMarker*100); xAxisPos > xAxisMin; xAxisPos -= this.unitsPerMarker*100) {
            welt.moveTo(xAxisPos, -this.markSize/2);
            welt.lineTo(xAxisPos, this.markSize/2);
            welt.stroke();
            welt.fillText(String(xAxisPos), xAxisPos, -this.markSize/2);
        }
        this.ctx.textAlign = 'right';
        this.ctx.textBaseline = 'middle';
        for(var yAxisPos = 0+this.unitsPerMarker; yAxisPos < yAxisMax; yAxisPos += this.unitsPerMarker) {
            welt.moveTo(-this.markSize/2, yAxisPos);
            welt.lineTo(this.markSize/2, yAxisPos);
            welt.stroke();
            welt.fillText(String(yAxisPos), -this.markSize, yAxisPos);
        }
        for(var yAxisPos = 0-this.unitsPerMarker; yAxisPos > yAxisMin; yAxisPos -= this.unitsPerMarker) {
            welt.moveTo(-this.markSize/2, yAxisPos);
            welt.lineTo(this.markSize/2, yAxisPos);
            welt.stroke();
            welt.fillText(String(yAxisPos), -this.markSize, yAxisPos);
        }
        welt.restore();
    };

    // function to draw equations
    // function to draw equations
    this.drawEquation = function(equation, graphColor, graphLineWidth) {
        this.lastEqu = equation;
        welt.save();
        welt.beginPath();
        welt.moveTo(this.xAxisMin, equation(this.xAxisMin));
        for(var x = this.xAxisMin; x <= this.xAxisMax; x += 1/this.drawResolution) {
            welt.lineTo(x, equation(x));
        }
        this.ctx.strokeStyle = graphColor;
        this.ctx.lineWidth = graphLineWidth;
        welt.stroke();
        welt.restore();
    }

    this.drawArray = function(array, graphColor, graphLineWidth) {
        welt.save();
        welt.beginPath();
        welt.moveTo(0, array[0]);
        for (var i = 1; i < array.length; i++) {
            welt.lineTo(i, array[i]);
        }
        this.ctx.strokeStyle = graphColor;
        this.ctx.lineWidth = graphLineWidth;
        welt.stroke();
        welt.restore();
    }

    this.drawAxis();
}
