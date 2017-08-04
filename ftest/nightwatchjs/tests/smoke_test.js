module.exports = {
    'Sample Smoke Test' : function (browser) {
        var nuxeo = browser.page.nuxeo();
        nuxeo.navigate()
        .waitForElementVisible('body', 1000)
        .setValue('@username', 'Administrator')
        .setValue('@password', 'Administrator')
        .click('@submit')
        .waitForElementVisible('@domainTitle', 5000)
        .assert.title('Nuxeo Platform - Domain')
        .assert.containsText('@domainTitle', 'Domain');
        browser.end();
    }
}