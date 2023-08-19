package server

import (
	"net/http"

	v1 "github.com/simon-duchastel/habit-tracker/server/api/v1"
)

// Proxy functions to allow Function deployments (functions must be callable from root)

func HelloHTTP(w http.ResponseWriter, r *http.Request) {
	v1.HelloHTTP(w, r)
}
