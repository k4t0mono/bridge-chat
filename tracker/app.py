from flask import Flask, jsonify, request
from base64 import b64decode
from invalid_usage import InvalidUsage
from user import User
from pprint import pprint
import jwt
import os
import json

app = Flask(__name__)

users = []
online = {}

if os.environ['FLASK_ENV'] == 'test':
    @app.route('/purge')
    def purge():
        users.clear()
        online.clear()

        return 'ok'

def inspect_users():
    print('[')

    for u in users:
        print('\t<User login=\'{}\'>'.format(u.login))
        for t in u.tokens:
            print('\t\t\<Token {}>'.format(t))
        print('\t</User>')

    print(']')

def auth(t):
    try:
        r = jwt.decode(t, os.environ['BRIDGECHAT_SECRET'], algorithms=['HS512'])
    except (jwt.exceptions.DecodeError, jwt.exceptions.InvalidSignatureError):
        return (False, None)

    l = [u for u in users if u.login == r['login']]
    if len(l) and t in l[0].tokens:
        return (True, l[0].login)

    return (False, None)


@app.errorhandler(InvalidUsage)
def handle_invalid_usage(error):
    response = jsonify(error.to_dict())
    response.status_code = error.status_code
    return response


@app.route('/')
def home():
    return 'Hewo'


@app.route('/user', methods=['POST'])
def new_user():
    try:
        login = request.headers['X-Auth-Login']
    except KeyError:
        raise InvalidUsage('Invalid data', status_code=400)

    l = [x for x in users if x.login == login]
    if len(l):
        raise InvalidUsage('User alredy exists', status_code=401)

    u = User(login)
    t = u.gen_token()
    users.append(u)

    res = jsonify({ 'token': t })
    return res


@app.route('/broadcast', methods=['POST'])
def broadcast():
    try:
        token = request.headers['X-Auth-Token']
    except KeyError:
        raise InvalidUsage('Invalid auth', status_code=401)

    (res, login) = auth(token)
    if not res:
        raise InvalidUsage('Invalid auth', status_code=401)

    try:
        data = json.loads(request.data.decode('utf-8'))
        ip = data['ip']
        port = data['port']
        op = data['op']
    except (json.decoder.JSONDecodeError, KeyError):
        raise InvalidUsage('Invalid body', status_code=400)

    if op == 1:
        online[login] = (ip, port)
    elif op == 0:
        online.pop(login)

    pprint(online)
    res = jsonify(success=True)
    return res

@app.route('/online')
def get_online():
    try:
        token = request.headers['X-Auth-Token']
    except KeyError:
        raise InvalidUsage('Invalid auth', status_code=401)

    (res, login) = auth(token)
    if not res:
        raise InvalidUsage('Invalid auth', status_code=401)

    l = []
    for k in online:
        if k != login:
            l.append({
                'login': k,
                'ip': online[k][0],
                'port': online[k][1],
            })

    res = jsonify(l)
    return res


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True, ssl_context=('./certf/cert.pem', './certf/key.pem'))
