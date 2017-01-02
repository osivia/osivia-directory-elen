$JQry(function() {
	
	
	var $filter = $JQry(".person-management .filter");
	
	searchPers($filter);
	
	$filter.keyup(function(event) {
		
		var $element = $JQry(event.target);
				
		searchPers($element);
		
	});
	
	

});

function searchPers($element) {
	
	jQuery.ajax({
		url: $element.data("url"),
		async: false,
		cache: true,
		headers: {
			"Cache-Control": "max-age=30, public"
		},
		data: {filter: $element.val()},
		dataType: "html",
		success : function(data, status, xhr) {
			
			$JQry("#personResults").html(data);
			$JQry(".person-link").click(function(event) {
				
				
				var $element = $JQry(event.target),
				uid = $element.data("uid");

				console.log(uid);
				$JQry("#selectedPerson").val(uid);
				
				
				$element.closest("form").find("[type=submit]").click();
			});
		}
	});
} 

