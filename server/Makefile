# deploy the habit-tracker server
deploy : deploy-functions deploy-endpoints

deploy-functions: deploy-v1-get-habits

deploy-v1-get-habits: api/v1/get-habits.go
	gcloud functions deploy v1-get-habits --gen2 --runtime=go120 --region=us-west1 --source=. --entry-point=HelloHTTP --trigger-http --project=simon-duchastel-habit-tracker

deploy-endpoints: api/openapi-functions.yaml
	gcloud endpoints services deploy api/openapi-functions.yaml --project simon-duchastel-habit-tracker
	./gcloud_build_image -s habit-tracker-5bp5c3riyq-uw.a.run.app -c 2023-08-20r0 -p simon-duchastel-habit-tracker
	gcloud run deploy habit-tracker --image="gcr.io/simon-duchastel-habit-tracker/endpoints-runtime-serverless:2.45.0-habit-tracker-5bp5c3riyq-uw.a.run.app-2023-08-20r0" --allow-unauthenticated --platform managed --project=simon-duchastel-habit-tracker