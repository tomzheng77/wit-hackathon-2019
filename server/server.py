from flask import Flask, render_template, request
from flask_cors import CORS, cross_origin
from werkzeug import secure_filename
from imageai.Detection import ObjectDetection
import base64
import os
import uuid
import json
app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

execution_path = os.getcwd()

detector = ObjectDetection()
detector.setModelTypeAsYOLOv3()
detector.setModelPath(os.path.join(execution_path , "yolo.h5"))
detector.loadModel()

@app.route('/upload', methods = ['POST'])
@cross_origin()
def upload_file():
	random_uuid = str(uuid.uuid4())
	path_in = 'upload/' + random_uuid + '.png'
	path_out = 'upload/' + random_uuid + '.out.png'
	body = request.data
	body = body.decode('UTF-8')
	body = base64.b64decode(body)
	with open(path_in, "wb") as f:
		f.write(body)

	detections = detector.detectObjectsFromImage(
		input_image=path_in,
		output_image_path=path_out,
		minimum_percentage_probability=30
	)

	result = []
	for eachObject in detections:
		# print(eachObject)
		# print(eachObject["name"] , " : ", eachObject["percentage_probability"], " : ", eachObject["box_points"] )
		# print("--------------------------------")
		a, b, c, d = eachObject['box_points']
		item = {}
		item['name'] = eachObject['name']
		item['percentage_probability'] = eachObject['percentage_probability']
		item['box_points'] = [
			int(a), int(b), int(c), int(d)
		]
		result.append(item)

	result = json.dumps(result)
	return result
		
if __name__ == '__main__':
	app.run(host="0.0.0.0", port=5000, debug = False, threaded = False, ssl_context='adhoc')
