package utils

import "fmt"

func LogError(message string) {
	fmt.Printf(`{"message": "%v", "severity": "error"}\n`, message)
}

func LogInfo(message string) {
	fmt.Printf(`{"message": "%v", "severity": "info"}\n`, message)
}
