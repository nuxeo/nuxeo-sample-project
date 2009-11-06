var currentPage = 0;
var errors=0;

function getNuxeoServerSideUrl() {
    return top.nxServerSideUrl;
}

function getNuxeoClientSideUrl() {
    return top.nxBaseUrl;
}

function getCurrentDomain() {
    return top.nxDomain;
}

function getUserLang() {
    return top.nxUserLang;
}

function getRestletUrl() {
    var ts = new Date().getTime() + "" + Math.random()*11
    var url = getNuxeoServerSideUrl() + "nuxeo/restAPI/lastBook/";
    url+=QM_Name + "?page="+ currentPage;
    if (getCurrentDomain()!=null && getCurrentDomain()!="") {
        url+="&domain=" + getCurrentDomain();
    }
    if (getUserLang()!=null && getUserLang()!="") {
        url+="&lang=" + getUserLang();
    }
    url+="&ts=" + ts;
    return url;
}

function getImageBaseUrl() {
    return getNuxeoClientSideUrl();
}

function getBaseUrl() {
    return getNuxeoClientSideUrl();
}

function getLastBook() {
    //console.log("calling REST API go to QM " + QM_Name);
    var params = {};
    var headers = {};

    params[gadgets.io.RequestParameters.AUTHORIZATION] = gadgets.io.AuthorizationType.NONE;
    params[gadgets.io.RequestParameters.CONTENT_TYPE] = gadgets.io.ContentType.JSON;

    var now = new Date().toUTCString();
    headers["Date", now];

    headers["Expires", "Fri, 01 Jan 1990 00:00:00 GMT"];
    headers["Pragma", "no-cache"];
    headers["Cache-control"] = "no-cache, must-revalidate";
    headers["X-NUXEO-INTEGRATED-AUTH"] = readCookie("JSESSIONID");

    params[gadgets.io.RequestParameters.HEADERS] = headers;

    var url = getRestletUrl();
    gadgets.io.makeRequest(url, handleJSONResponse, params);
};

function handleJSONResponse(obj) {
	var jsonObject = obj.data;
	if (jsonObject == null) {
		if (errors == 0) {
			errors = 1;
			getLastBook();
		} else {
			//  alert("Error, no result from server : " + obj.errors);
			_gel("bookData").innerHTML = "There is no Book Document, please create one with a valid isbn number.";
		}
		return;
	} else {
		errors = 0;
	}
	displayLastBook(jsonObject);
}

function displayLastBook(jsonObject) {
	var book = jsonObject.book;
	var htmlContent = "<img  height=\"150px\" src=\"http://covers.openlibrary.org/b/isbn/"
			+ book.isbn + "-M.jpg\" />"
	_gel("bookData").innerHTML = htmlContent;
	htmlContent = "<div> title: ";
	htmlContent += book.title;
	htmlContent += "</div>";
	htmlContent += "<div> isbn: ";
	htmlContent += book.isbn;
	htmlContent += "</div>";
	_gel("bookInfo").innerHTML = htmlContent;
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for ( var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ')
            c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0)
            return c.substring(nameEQ.length, c.length);
    }
    return null;
}
gadgets.util.registerOnLoadHandler(getLastBook);
