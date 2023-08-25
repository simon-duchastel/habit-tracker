# deploy the habit-tracker server
deploy : deploy-functions deploy-endpoints

# deploy each of the API functions
deploy-functions: deploy-v1-get-habits

deploy-v1-get-whoami: api/v1/whoami-get.go
	gcloud functions deploy v1-get-whoami --gen2 --runtime=go120 --region=us-west1 --source=. --entry-point=GetWhoAmIV1 --trigger-http --project=simon-duchastel-habit-tracker

deploy-v1-get-habits-summary: api/v1/habits-summary-get.go
	gcloud functions deploy v1-get-habits-summary --gen2 --runtime=go120 --region=us-west1 --source=. --entry-point=GetHabitsSummaryV1 --trigger-http --project=simon-duchastel-habit-tracker

deploy-v1-get-habits-for-day: api/v1/habits-for-day-get.go
	gcloud functions deploy v1-get-habits-for-day --gen2 --runtime=go120 --region=us-west1 --source=. --entry-point=GetHabitsForDayV1 --trigger-http --project=simon-duchastel-habit-tracker

deploy-v1-post-habits-for-day: api/v1/habits-for-day-post.go
	gcloud functions deploy v1-post-habits-for-day --gen2 --runtime=go120 --region=us-west1 --source=. --entry-point=PostHabitsForDayV1 --trigger-http --project=simon-duchastel-habit-tracker

deploy-v1-get-goals: api/v1/goals-get.go
	gcloud functions deploy v1-get-goals --gen2 --runtime=go120 --region=us-west1 --source=. --entry-point=GetGoalsV1 --trigger-http --project=simon-duchastel-habit-tracker

deploy-v1-post-goals: api/v1/goals-post.go
	gcloud functions deploy v1-post-goals --gen2 --runtime=go120 --region=us-west1 --source=. --entry-point=PostGoalsV1 --trigger-http --project=simon-duchastel-habit-tracker

# NOTE - this doesn't currently work. It should be moved to a shell script or something, but is left here for reference
# deploy the endpoints (ie. gateway) to access the APIs
# deploy-endpoints: api/openapi-functions.yaml
# 	gcloud endpoints services deploy api/openapi-functions.yaml --project simon-duchastel-habit-tracker
# 	./gcloud_build_image -s habit-tracker-5bp5c3riyq-uw.a.run.app -c ${CID} -p simon-duchastel-habit-tracker
# 	gcloud run deploy habit-tracker --image="gcr.io/simon-duchastel-habit-tracker/endpoints-runtime-serverless:2.45.0-habit-tracker-5bp5c3riyq-uw.a.run.app-${CID}" --allow-unauthenticated --platform managed --project=simon-duchastel-habit-tracker