
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

public class Joystick {

	private int I2CSlaveAddress = 0x08; // You must add a 0-bit after the arduino address (in binary) 0x04 (100)=>(1000)
										// 0x08
	I2CSensor arduino;
	private byte[] buf = new byte[5];

	private float xValue = 0; // variable to store x value
	private float yValue = 0; // variable to store y value
	private float pushed;

	Joystick(Port S) {
		this.arduino = new I2CSensor(S, I2CSlaveAddress);

	}

	private void actualisation() {

		arduino.getData('B', buf, buf.length);
		arduino.setRetryCount(10);

		xValue = buf[1] << 8 | (buf[0] & 0xFF);
		yValue = buf[3] << 8 | (buf[2] & 0xFF);
		pushed = buf[4];

	}

	public float[] getSample() {
		actualisation();
		float[] sample = { xValue, yValue, pushed };
		return sample;
	}

	public void close() {
		arduino.close();
	}

}