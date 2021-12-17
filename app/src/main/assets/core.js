function showAlert() {
            alert(document.getElementById("android").value);
}

function showWebAlert(message) {
    alert(message);
}

function showAndroidToast() { <!-- Sending value to Android -->
    WebAppInterface.showToast(document.getElementById("android").value);
}

function showAndroidVersion(message) { <!-- Getting value from Android -->
    var androidVersion = WebAppInterface.getAndroidVersion();
    document.getElementById("paragraph").innerHTML = message + " You are running API Version " + androidVersion;
}

function sendToAndroid() {
    WebAppInterface.postLiveText(document.getElementById("android").value);
}

function setParagraph(paragraph) {
    document.getElementById("paragraph").innerHTML = paragraph;
}