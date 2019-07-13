/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

'use strict';

var hdVideo = document.querySelector('video#hd');
const canvas = document.createElement('canvas');

var hdConstraints = {
  video: {
    mandatory: {
      minWidth: 320,
      minHeight: 180
    }
  }
};

function errorCallback(error) {
  console.log('navigator.getUserMedia error: ', error);
}


function screenshot(video) {
  canvas.width = video.videoWidth;
  canvas.height = video.videoHeight;
  canvas.getContext('2d').drawImage(video, 0, 0);
  // Other browsers will fall back to image/png
  console.log(canvas.toDataURL('image/webp'));
  img.src = canvas.toDataURL('image/webp');
}

navigator.mediaDevices.getUserMedia(
  hdConstraints
).then(
  function(stream) {
    hdVideo.srcObject = stream;
    hdVideo.play();
  },
);

setTimeout(() => {
  screenshot(hdVideo);
})
