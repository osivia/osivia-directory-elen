$JQry(function() {
	
	$JQry(".workspace-member-management select.select2").each(function(index, element) {
		var $element = $JQry(element),
			url = $element.data("url"),
 			options = {
					minimumInputLength : 3,
					theme : "bootstrap"
				};
		
		// Ajax
		options["ajax"] = {
			url : url,
			dataType: "json",
			delay: 250,
			data: function(params) {
				return {
					filter: params.term,
				};
			},
			processResults: function(data, params) {				
				return {
					results: data
				};
			},
			cache: true
		};
		
		
		// Result template
		options["templateResult"] = function(params) {
			$result = $JQry(document.createElement("div"));
			
			if (params.loading) {
				$result.text(params.text);
			} else {
				$result.addClass("person");
				
				// Person avatar
				$personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($result);
				
				if (params.create) {
					// Icon
					$icon = $JQry(document.createElement("i"));
					$icon.addClass("glyphicons glyphicons-user-add");
					$icon.text("");
					$icon.appendTo($personAvatar);
				} else if (params.avatar !== undefined) {
					// Avatar
					$avatar = $JQry(document.createElement("img"));
					$avatar.attr("src", params.avatar);
					$avatar.attr("alt", "");
					$avatar.appendTo($personAvatar);
				}
				
				// Person title
				$personTitle = $JQry(document.createElement("div"));
				$personTitle.addClass("person-title");
				$personTitle.text(params.displayName)
				$personTitle.appendTo($result);
				
				// Person extra
				$personExtra = $JQry(document.createElement("div"));
				$personExtra.addClass("person-extra");
				if (params.create) {
					$personExtra.text(params.extra);
				} else {
					text = params.id;
					if (params.mail !== undefined) {
						text += " – ";
						text += params.mail;
					}
					
					$personExtra.text(text);
				}
				$personExtra.appendTo($result);
			}

			return $result;
		};
		
		
		// Selection template
		options["templateSelection"] = function(params) {
			// Selection
			$selection = $JQry(document.createElement("div"));
			$selection.addClass("person");
			
			if (params.avatar !== undefined) {
				// Person avatar
				$personAvatar = $JQry(document.createElement("div"));
				$personAvatar.addClass("person-avatar");
				$personAvatar.appendTo($selection);
				
				// Avatar
				$avatar = $JQry(document.createElement("img"));
				$avatar.attr("src", params.avatar);
				$avatar.attr("alt", "");
				$avatar.appendTo($personAvatar);
			}
			
			// Person title
			$personTitle = $JQry(document.createElement("div"));
			$personTitle.addClass("person-title");
			if (params.create) {
				$personTitle.text(params.id);
			} else if (params.displayName === undefined) {
				$personTitle.text(params.text);
			} else {
				$personTitle.text(params.displayName);
			}
			$personTitle.appendTo($selection);
			
			return $selection;
		};

		
		// Internationalization
		options["language"] = {};
		if ($element.data("input-too-short") !== undefined) {
			options["language"]["inputTooShort"] = function() {
				return $element.data("input-too-short");
			}
		}
		if ($element.data("error-loading") !== undefined) {
			options["language"]["errorLoading"] = function() {
				return $element.data("error-loading");
			}
		}
		if ($element.data("loading-more") !== undefined) {
			options["language"]["loadingMore"] = function() {
				return $element.data("loading-more");
			}
		}
		if ($element.data("searching") !== undefined) {
			options["language"]["searching"] = function() {
				return $element.data("searching");
			}
		}
		if ($element.data("no-results") !== undefined) {
			options["language"]["noResults"] = function() {
				return $element.data("no-results");
			}
		}
		
		
		// Force width to 100%
		$element.css({
			width: "100%"
		});
		
		
		$element.select2(options);
		
		
		// Display collapsed buttons
		$element.on("select2:opening", function(event) {
			var $form = $element.closest("form"),
				$collapse = $form.find(".collapse");
			
			if (!$collapse.hasClass("in")) {
				$collapse.collapse("show");
			}
		});
	});
	
	
	$JQry(".workspace-member-management select").change(function(event) {
		var $target = $JQry(event.target),
			$form = $target.closest("form"),
			$collapse = $form.find(".collapse");
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});

	
	$JQry(".workspace-member-management button.delete").click(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $target.closest("fieldset"),
			$row = $fieldset.closest(".table-row"),
			$hidden = $row.find("input[type=hidden]"),
			$form = $fieldset.closest("form"),
			$collapse = $form.find(".collapse");
		
		$hidden.val(true);
		$fieldset.prop("disabled", true);
		$collapse.collapse("show");
	});
	
	
	$JQry(".workspace-member-management button[type=reset]").click(function(event) {
		var $target = $JQry(event.target),
			$form = $target.closest("form");
		
		$form.find("fieldset[disabled]").prop("disabled", false);
		$form.find("input[type=hidden][value=true]").val(false);
	});
	
});
