function refreshOnChange( selectText1, selectText2, selectText3, url) {
    
    url = url.replace( "SELECTED_TEXT1", selectText1.options[selectText1.selectedIndex].value);
    url = url.replace( "SELECTED_TEXT2", selectText2.options[selectText2.selectedIndex].value);
    url = url.replace( "SELECTED_TEXT3", selectText3.value);
    
    updatePortletContent( selectText1, url);
     
    return false;

}

function ajouterGroupe( selectText1, url) {
    
    url = url.replace( "SELECTED_TEXT1", selectText1.options[selectText1.selectedIndex].value);
    updatePortletContent( selectText1, url);
     
    return false;

}