from flask import Flask, jsonify, request
from base64 import b64decode
from invalid_usage import InvalidUsage
from user import User
from pprint import pprint
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
        data = request.headers['Authorization'].split()
        (login, passw) = b64decode(data[1]).decode('utf-8').split(':')
    except KeyError:
        raise InvalidUsage('Invalid data', status_code=400)

    l = [x for x in users if x.login == login]
    if len(l):
        raise InvalidUsage('User alredy exists', status_code=401)

    u = User(login, passw)
    t = u.gen_token()
    users.append(u)

    res = jsonify({ 'token': t })
    return res


@app.route('/broadcast', methods=['POST'])
def broadcast():
    try:
        login = request.headers['X-Auth-Login']
        token = request.headers['X-Auth-Token']
    except KeyError:
        raise InvalidUsage('Invalid auth', status_code=401)

    l = [u for u in users if u.login == login]
    if len(l) == 0 or not l[0].auth(token):
        raise InvalidUsage('Invalid auth', status_code=401)

    try:
        data = json.loads(request.data.decode('utf-8'))
        ip = data['ip']
        port = data['port']
    except (json.decoder.JSONDecodeError, KeyError):
        raise InvalidUsage('Invalid body', status_code=400)
    
    online[login] = (ip, port)
    pprint(online)

    res = jsonify(success=True)
    return res

if __name__ == '__main__':
    app.run(debug=True, ssl_context=('./certf/cert.pem', './certf/key.pem'))
