# Large Response Issue

To build docker image:

docker build -t large_response .  

To run the image

docker run -d --memory="4g" --cpus="2" -p 8888:8080 large_response

Check the performance and cpu while running the test. Performance drops and CPU usage is high as well
