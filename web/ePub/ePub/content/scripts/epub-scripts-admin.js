//funktion für upload progress bar und status
(function() {
    
	var bar = $('.bar');
	var percent = $('.percent');
	var status = $('#status');
	   
	$('#upload-form').ajaxForm({
		beforeSend: function() {
			status.empty();
			var percentVal = '0%';
			bar.width(percentVal)
			percent.html(percentVal);
		},
		uploadProgress: function(event, position, total, percentComplete) {
			var percentVal = percentComplete + '%';
			bar.width(percentVal)
			percent.html(percentVal);
		},
		complete: function(xhr) {
			status.html(xhr.responseText);
			$('#upload-form').resetForm();
		}
	}); 

})(); 
