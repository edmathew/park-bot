package brain;

import java.util.Random;

import activation.ActivationFunction;
import activation.ActivationSigmoid;
import matrix.Matrix;
import matrix.MatrixMath;

public class FeedForwardLayer {
	private int neuronNumber;
	private Matrix matrix;
	private double[] charge;
	private FeedForwardLayer previousLayer;
	private FeedForwardLayer nextLayer;
	private ActivationFunction activationFunction;

	public FeedForwardLayer(int neuronNumber){
		this(neuronNumber, new ActivationSigmoid());
	}

	public FeedForwardLayer(int neuronNumber, ActivationFunction function){
		this.neuronNumber = neuronNumber;
		charge = new double[neuronNumber];
		this.activationFunction = function;
	}

	public void setMatrix(Matrix matrix){
		this.matrix = matrix;
	}

	public void setPreviousLayer(FeedForwardLayer ffl) {
		this.previousLayer = ffl;
	}

	public void setNextLayer(FeedForwardLayer ffl) {
		this.nextLayer = ffl;
	}

	public boolean isInput(){
		return previousLayer == null;
	}

	public boolean isHidden(){
		return previousLayer != null && nextLayer != null;
	}

	public boolean isOutput(){
		return nextLayer == null;
	}

	public void calculate(double[] input) {
		if (input != null){
			//first layer, recebe o input, n�o tem matriz, a carga dos seus neur�nios � o valor de input
			this.charge = input;
		}else{
			//layers restantes, usa a carga das layers anteriores para dar input aos seus neur�nios
			//ponderar no novo m�dulo de matrizes devolver as colunas inteiras para permitir itera��o
			for (int i = 0 ; i < matrix.getCols(); i++){
				this.setCharge(i, MatrixMath.dotProduct(formMatrix(previousLayer.getCharge()), matrix.getCol(i)));
			}
		}
	}

	public Matrix formMatrix(double[] input){
		Matrix inputVector = new Matrix(1, input.length);
		for (int i = 0; i < input.length; i++)
			inputVector.set(0, i, input[i]);
		return inputVector;
	}

	private void setCharge(int pos, double charge) {
		//que carga � que estes neur�nios v�o passar aos neur�nios da camada seguinte?
		//vai depender da fun��o de activa��o
		if (!isOutput())
			this.charge[pos] = activationFunction.activationFunction(charge);
		else
			this.charge[pos] = charge;
	}

	public double[] getCharge() {
		return charge;
	}

	public void randomize() {
		if (previousLayer != null){
			Random r = new Random();

			double[][] temp = new double[previousLayer.charge.length][neuronNumber];
			for (int i = 0; i < previousLayer.charge.length; i++){
				for (int j = 0 ; j < neuronNumber; j++){
					temp[i][j] = r.nextDouble();
				}
			}
		}
	}

}
