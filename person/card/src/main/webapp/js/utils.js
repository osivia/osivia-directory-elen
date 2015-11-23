function confirmRaz(selectedText, url) {
	var answer = confirm("Ré-initialiser le mot de passe élève ?");
	if (answer) {
		updatePortletContent(selectedText,url);
	}
	return false;
}

function confirmSurcharge(selectedText, url) {
	var answer = confirm("Surcharger le mot de passe de cet élève ?");
	if (answer) {
		updatePortletContent(selectedText,url);
	}
	return false;
}

function changeBoolean(){
	var form = document.getElementById("formChgtAvatarProfil");
	this.document.getElementById('chargementAvatar').value = true;
	form.submit();
}
