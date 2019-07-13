import requests
import os 
URL = "http://localhost:5000/upload"
dir_path = os.path.dirname(os.path.realpath(__file__))
FILE = os.path.join(dir_path, 'images/ex.png')
files = {'media': open(FILE, 'rb')}

try:
  r = requests.post(URL, files=files)
  print(r.text)
except:
  print('exception!')
#print(r)

