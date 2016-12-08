module.exports = {
  url: 'http://127.0.0.1:8080/nuxeo',
  elements: {
    username: {
      selector: '//input[@id="username"]',
      locateStrategy: 'xpath'
    },
    password: {
      selector: '//input[@id="password"]',
      locateStrategy: 'xpath'
    },
    submit: {
      selector: '//input[@class="login_button"]',
      locateStrategy: 'xpath'
    },
    welcomemsg: {
      selector: '//div[@class="copyrights"]',
      locateStrategy: 'xpath'
    }
  }
};