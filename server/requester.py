import requests

with open('image2.jpg', 'rb') as f:
	binary = f.read()
	requests.post(url='http://localhost:5000/upload', data=binary)
