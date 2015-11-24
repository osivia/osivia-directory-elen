function refreshOnChangeGestionOrga( selectText, url) {
    
    url = url.replace( "SELECTED_TEXT", selectText.options[selectText.selectedIndex].value);
    
    updatePortletContent( selectText, url);
     
    return false;

}