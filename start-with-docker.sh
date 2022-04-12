./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=asouzamax/hangman && docker run -p 8080:8080 asouzamax/hangman
