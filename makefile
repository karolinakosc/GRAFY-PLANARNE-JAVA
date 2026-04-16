all:
	gcc -Wall -Wextra -std=c18 src/main.c src/graf.c src/log.c src/matrix_operations.c src/planar.c src/pob_dane.c src/spectral.c src/triangulation.c src/zapis.c src/utlis.c -o program -lm
test_spec:
	gcc -lm -Wall -Wextra -std=c18 src/main.c src/graf.c src/log.c src/matrix_operations.c src/planar.c src/pob_dane.c src/spectral.c src/utlis.c -o test_program -lm
clean:
	rm -f program
