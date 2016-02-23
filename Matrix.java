import java.util.Random;

public class Matrix{

	public static void main(String[] args){
		System.out.printf("%20s%20s%20s%20s\n", "Size", "Classic", "DivConq", "Strassen's");
		for(int i = 1; i < Integer.parseInt(args[0]); i++){
			doIt(i);
		}
	}

	private static void doIt(int power){
		int classicTotal = 0;
		int divConqTotal = 0;
		int strassensTotal = 0;
		Random random = new Random();
		int n = (int)Math.pow(2.0, power);
	       	System.out.printf("%20d", n);
		for(int z = 0; z < 5; z++){
			int[][] matrix1 = new int[n][n];
			int[][] matrix2 = new int[n][n];
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					matrix1[i][j] = random.nextInt(100);
					//System.out.print(matrix1[i][j] + " ");
					//if(i == j)
					matrix2[i][j] = random.nextInt(100);
				}
			}
			long start = System.currentTimeMillis();
			classic(matrix1, matrix2);
			classicTotal += System.currentTimeMillis() - start;
			start = System.currentTimeMillis();
			divConq(matrix1, matrix2);
			divConqTotal += System.currentTimeMillis() - start;
			start = System.currentTimeMillis();
			strassens(matrix1, matrix2);
			strassensTotal += System.currentTimeMillis() - start;
		}
//		classicTotal /= 5;
//		divConqTotal /= 5;
//		strassensTotal /= 5;
		System.out.printf("%20d", classicTotal /= 5);
		System.out.printf("%20d", divConqTotal /= 5);
		System.out.printf("%20d", strassensTotal /= 5);
		System.out.println();
	}

	//obsolete print method for testing purposes
	private static void print(int[][] mat){
		for(int i = 0; i < mat.length; i++){
			for(int e: mat[i])
				System.out.printf("%7d ", e);
			System.out.println();
		}
	}

	private static int[][] classic(int[][] mat1, int[][] mat2){
		int[][] result = new int[mat1.length][mat1.length];
		for(int i = 0; i < result.length; i++){
			for(int j = 0; j < result[i].length; j++){
				int sum = 0;
				for(int k = 0; k < mat2.length; k++)
					sum += mat1[i][k]*mat2[k][j];
				result[i][j] = sum;
			}
		}
		return result;
	}

	private static int[][] divConq(int[][] mat1, int[][] mat2){
		if(mat1.length == 2)
			return classic(mat1, mat2);
		// A	B
		// C	D
		int[][] matA = add(divConq(getMat('A', mat1), getMat('A', mat2)), divConq(getMat('B', mat1), getMat('C', mat2)));
		int[][] matB = add(divConq(getMat('A', mat1), getMat('B', mat2)), divConq(getMat('B', mat1), getMat('D', mat2)));
		int[][] matC = add(divConq(getMat('C', mat1), getMat('A', mat2)), divConq(getMat('D', mat1), getMat('C', mat2)));
		int[][] matD = add(divConq(getMat('C', mat1), getMat('B', mat2)), divConq(getMat('D', mat1), getMat('D', mat2)));
		return assemble(matA, matB, matC, matD);
	}

	private static int[][] add(int[][] mat1, int[][] mat2){
		int[][] result = new int[mat1.length][mat1.length];
		for(int i = 0; i < result.length; i++){
			for(int j = 0; j < result[i].length; j++){
				result[i][j] = mat1[i][j] + mat2[i][j];
			}
		}
		return result;
	}

	private static int[][] subtract(int[][] mat1, int[][] mat2){
		int[][] result = new int[mat1.length][mat1.length];
		for(int i = 0; i < result.length; i++){
			for(int j = 0; j < result[i].length; j++){
				result[i][j] = mat1[i][j] - mat2[i][j];
			}
		}
		return result;
	}


	private static int[][] getMat(char which, int[][] mat){
		int[][] mini = new int[mat.length/2][mat.length/2];
		for(int i = 0; i < mini.length; i++){
			for(int j = 0; j < mini.length; j++){
				switch(which){
					case'A': mini[i][j] = mat[i][j];
						break;
					case'B': mini[i][j] = mat[i][mini.length + j];
						break;
					case'C': mini[i][j] = mat[mini.length + i][j];
						break;
					case'D': mini[i][j] = mat[mini.length + i][mini.length + j];
						break;
				}
			}
		}
		return mini;
	}

	private static int[][] assemble(int[][] matA, int[][] matB, int[][] matC, int[][] matD){
		int[][] result = new int[matA.length*2][matA.length*2];
		for(int i = 0; i < result.length; i++){
			for(int j = 0; j < result.length; j++){
				if(i < matA.length && j < matA.length)
					result[i][j] = matA[i][j];
				else if(i < matA.length && j >= matA.length)
					result[i][j] = matB[i][j-matB.length];
				else if(i >= matA.length && j < matA.length)
					result[i][j] = matC[i-matC.length][j];
				else
					result[i][j] = matD[i-matD.length][j-matD.length];
			}
		}
		return result;
	}

	private static int[][] strassens(int[][] mat1, int[][] mat2){
		if(mat1.length == 2)
			return classic(mat1, mat2);
		int[][] P = strassens(add(getMat('A', mat1), getMat('D', mat1)), add(getMat('A', mat2), getMat('D', mat2)));
		int[][] Q = strassens(add(getMat('C', mat1), getMat('D', mat1)), getMat('A', mat2));
		int[][] R = strassens(getMat('A', mat1), subtract(getMat('B', mat2), getMat('D', mat2)));
		int[][] S = strassens(getMat('D', mat1), subtract(getMat('C', mat2), getMat('A', mat2)));
		int[][] T = strassens(add(getMat('A', mat1), getMat('B', mat1)), getMat('D', mat2));
		int[][] U = strassens(subtract(getMat('C', mat1), getMat('A', mat1)), add(getMat('A', mat2), getMat('B', mat2)));
		int[][] V = strassens(subtract(getMat('B', mat1), getMat('D', mat1)), add(getMat('C', mat2), getMat('D', mat2)));
		int[][] A = add(subtract(add(P, S), T), V);
		int[][] B = add(R, T);
		int[][] C = add(Q, S);
		int[][] D = add(subtract(add(P, R), Q), U);
		return assemble(A, B, C, D);
	}

}
