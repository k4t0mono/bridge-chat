import jwt
import time
import os

class User():

    def __init__(self, login, passw):
            self.login = login
            self.passw = passw
            self.tokens = []

    def gen_token(self):
        end = int(str(time.time())[:-8]) + 86400 
        d = { 'login': self.login, 'type': 'auth', 'time': end }
        t = jwt.encode(d, os.environ['BRIDGECHAT_SECRET'], algorithm='HS512')
        t = t.decode('utf-8')

        self.tokens.append(t)

        return t
    
    def __repr__(self):
        s = '<User login=\'{}\' tokens={}>'.format(self.login, len(self.tokens))
        return s
