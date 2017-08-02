# -*- coding: iso-8859-15 -*-
"""Sample test

"""
import os
import unittest
import random
from funkload.FunkLoadTestCase import FunkLoadTestCase
from webunit.utility import Upload
from funkload.utils import Data
from funkload.utils import xmlrpc_get_credential


class Sample(FunkLoadTestCase):
    """XXX

    This test use a configuration file Sample.conf.
    """
    def setUp(self):
        """Setting up test."""
        self.logd("setUp")
        self.server_url = self.conf_get('main', 'url')
        credential_host = self.conf_get('credential', 'host')
        credential_port = self.conf_getInt('credential', 'port')
        self.login, self.password = xmlrpc_get_credential(credential_host,
                                                          credential_port,
                                                          'admin')

    def testAvailable(self):
        server_url = self.server_url
        self.get(server_url, description="login page")

    def test_sample(self):
        # The description should be set in the configuration file
        server_url = self.server_url

        self.post(server_url + "/nxstartup.faces", params=[
            ['language', 'fr'],
            ['user_password', self.password],
            ['Submit', 'Connexion'],
            ['form_submitted_marker', ''],
            ['user_name', self.login],
            ['requestedUrl', '']],
            description="Login")
        self.assert_('LoginFailed=true' not in self.getLastUrl(),
                     'Login failed for %s:%s' % (self.login, self.password))

        self.get(server_url + "/logout",
            description="Log out")
        self.assert_('login' in self.getLastUrl(),
                     "Not redirected to login page.")
        # end of test -----------------------------------------------

    def tearDown(self):
        """Setting up test."""
        self.logd("tearDown.\n")


if __name__ in ('main', '__main__'):
    unittest.main()
