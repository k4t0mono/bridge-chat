import unittest
import requests
import jwt
import os

def get_session():
    requests.packages.urllib3.disable_warnings()
    s = requests.Session()
    s.verify = False
    s.get('{}/purge'.format(BASE_URL))

    return s

BASE_URL = 'https://localhost:5000'

class ServerTest(unittest.TestCase):
    def test_home(self):
        s = get_session()

        r = s.get('https://localhost:5000/')
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r.text, 'Hewo')

    def test_newUser_noAuth_badRequest(self):
        s = get_session()
        r = s.post('{}/user'.format(BASE_URL))

        self.assertEqual(r.status_code, 400)

    def test_newUser_validAuth_token(self):
        s = get_session()
        r = s.post('{}/user'.format(BASE_URL), auth=('user0', 'pass'))

        self.assertEqual(r.status_code, 200)

        t = jwt.decode(
                r.json()['token'],
                os.environ['BRIGECHAT_SECRET'],
                algorithms=['HS512']
        )
        self.assertEqual(t['login'], 'user0')
        self.assertEqual(t['type'], 'auth')

    def test_newUser_sameUser_userExists(self):
        s = get_session()
        r0 = s.post('{}/user'.format(BASE_URL), auth=('user1', 'pass'))
        r1 = s.post('{}/user'.format(BASE_URL), auth=('user1', 'pass'))

        self.assertEqual(r0.status_code, 200)
        self.assertEqual(r1.status_code, 401)

    def test_broadcast_noAuth_unauthorized(self):
        s = get_session()
        r = s.post('{}/broadcast'.format(BASE_URL))

        self.assertEqual(r.status_code, 401)

    def test_broadcast_invalidAuth_unauthorized(self):
        s = get_session()
        r0 = s.post('{}/user'.format(BASE_URL), auth=('user1', 'pass'))

        headers = {
            'X-Auth-Login': 'user',
            'X-Auth-Token': r0.json()['token']
        }
        r1 = s.post('{}/broadcast'.format(BASE_URL), headers=headers)

        self.assertEqual(r1.status_code, 401)

    def test_broadcast_invalidBody_badRequest(self):
        s = get_session()
        r0 = s.post('{}/user'.format(BASE_URL), auth=('user1', 'pass'))

        headers = {
            'X-Auth-Login': 'user1',
            'X-Auth-Token': r0.json()['token']
        }
        r1 = s.post('{}/broadcast'.format(BASE_URL), headers=headers)

        self.assertEqual(r1.status_code, 400)

    def test_broadcast_validBody_ok(self):
        s = get_session()
        r0 = s.post('{}/user'.format(BASE_URL), auth=('user1', 'pass'))

        headers = {
            'X-Auth-Login': 'user1',
            'X-Auth-Token': r0.json()['token']
        }

        body = { 'ip': '192.168.0.4', 'port': 53123, 'op': 1 }

        r1 = s.post('{}/broadcast'.format(BASE_URL), headers=headers, json=body)

        self.assertEqual(r1.status_code, 200)

    def test_getOnline_noAuth_unauthorized(self):
        s = get_session()

        r1 = s.get('{}/online'.format(BASE_URL))

        self.assertEqual(r1.status_code, 401)

    def test_getOnline_invalidAuth_unauthorized(self):
        s = get_session()
        r0 = s.post('{}/user'.format(BASE_URL), auth=('user1', 'pass'))

        headers = {
            'X-Auth-Login': 'user1',
            'X-Auth-Token': 'asd'
        }
        r1 = s.get('{}/online'.format(BASE_URL), headers=headers)

        self.assertEqual(r1.status_code, 401)

    def test_getOnline_validAuth_ok(self):
        s = get_session()
        r0 = s.post('{}/user'.format(BASE_URL), auth=('user1', 'pass'))

        headers = {
            'X-Auth-Login': 'user1',
            'X-Auth-Token': r0.json()['token']
        }
        r1 = s.get('{}/online'.format(BASE_URL), headers=headers)

        self.assertEqual(r1.status_code, 200)




if __name__ == '__main__':
    unittest.main()
