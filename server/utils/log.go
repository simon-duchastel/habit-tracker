package utils

import "fmt"

func LogError(message string) {
	fmt.Printf(`{"message": "%v", "severity": "ERROR"}`, message)
	fmt.Println()
}

func LogInfo(message string) {
	fmt.Printf(`{"message": "%v", "severity": "INFO"}`, message)
	fmt.Println()
}
