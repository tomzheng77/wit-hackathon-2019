from flask import Flask, escape, url_for, request
import json
app = Flask(__name__)

@app.route('/upload', methods=['POST'])
def upload():
  print(request.files)
  file = request.files['media']
  return json.dumps(
    [{
      'name': 'chair',
      'color': 'orange',
      'coordinates': [400, 250]
    }]
  )

# get_squares: todo

app.run()