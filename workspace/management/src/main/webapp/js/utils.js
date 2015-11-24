
function refreshThemeWks(selectText){
	var theme = selectText.options[selectText.selectedIndex].value;
	document.getElementById("previewThemeWks").className = theme;
	var imgSrcPrevious = document.getElementById("imagePreviewWks").src;
	var i = imgSrcPrevious.lastIndexOf("preview_acrennes");
	var imgSrc = imgSrcPrevious.substring(0,i)+"preview_"+theme+".png";
	document.getElementById("imagePreviewWks").src=imgSrc;
	return false;	
}

function ajouterGroupe( selectText1, url) {
    
    url = url.replace( "SELECTED_TEXT1", selectText1.options[selectText1.selectedIndex].value);
    updatePortletContent( selectText1, url);
     
    return false;

}

function updatePortlet1Param( selectText1, url) {
    
    url = url.replace( "SELECTED_TEXT1", selectText1.value);
    updatePortletContent( selectText1, url);
     
    return false;

}