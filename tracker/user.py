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
    
    def auth(self, t):
        l = [x for x in self.tokens if x == t]
        if  len(l) == 0:
            return False

        try:
            r = jwt.decode(t, os.environ['BRIGECHAT_SECRET'], algorithms=['HS512'])
        except jwt.exceptions.InvalidSignatureError:
            return False

        if r['login'] != self.login:
            return False
        
        return True

    def __repr__(self):
        s = '<User login=\'{}\' tokens={}>'.format(self.login, len(self.tokens))
        return s
