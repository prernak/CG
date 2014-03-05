//
//  openGLMain.java
//  
//
//  Bare bones framework for creating 3D scene in OpenGL. 
//
//

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.Buffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

public class openGLMain extends simpleShape implements GLEventListener,
		KeyListener {

	/**
	 * buffer info
	 */
	private int vbuffer[];
	private int ebuffer[];
	private boolean bufferInit = false;

	/**
	 * uniform variable data
	 */
	public float angles[];
	public float transData[];
	public float scaleData[];

	/**
	 * shader info
	 */
	private shaderProgram myShaders;
	private int shaderProgID = 0;
	private boolean updateNeeded = true;

	public int theta;
	public int trans;
	public int scale;
	/**
	 * my canvas
	 */
	GLCanvas myCanvas;

	/**
	 * constructor
	 */
	public openGLMain(GLCanvas G) {
		angles = new float[6];
		angles[0] = 60.0f;
		angles[1] = 45.0f;
		angles[2] = 120.0f;

		angles[3] = 105.0f;
		angles[4] = 105.0f;
		angles[5] = 100.0f;

		transData = new float[6];
		transData[0] = -100.0f;
		transData[1] = -100.0f;
		transData[2] = -100.0f;

		transData[3] = 100.0f;
		transData[4] = 100.0f;
		transData[5] = 100.0f;

		scaleData = new float[6];
		scaleData[0] = 0.75f;
		scaleData[1] = 0.75f;
		scaleData[2] = 0.75f;

		scaleData[3] = 0.75f;
		scaleData[4] = 0.75f;
		scaleData[5] = 0.75f;

		vbuffer = new int[2];
		ebuffer = new int[2];

		myShaders = new shaderProgram();
		myCanvas = G;

		G.addGLEventListener(this);
		G.addKeyListener(this);
	}

	private void errorCheck(GL2 gl2) {
		int code = gl2.glGetError();
		if (code == GL.GL_NO_ERROR)
			System.err.println("All is well");
		else
			System.err.println("Problem - error code : " + code);

	}

	/**
	 * Called by the drawable to initiate OpenGL rendering by the client.
	 */
	public void display(GLAutoDrawable drawable) {
		// get GL
		GL2 gl2 = (drawable.getGL()).getGL2();

		// clear and draw parameters...best to keep these
		gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl2.glCullFace(GL.GL_BACK);
		gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glLoadIdentity();

		// draw your shapes here.
		gl2.glViewport(0, 128, 256, 256);
		updateShape(0, gl2);
		gl2.glViewport(256, 128, 256, 256);
		updateShape(1, gl2);
	}

	/**
	 * Notifies the listener to perform the release of all OpenGL resources per
	 * GLContext, such as memory buffers and GLSL programs.
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized.
	 */
	public void init(GLAutoDrawable drawable) {
		// get the gl object
		GL2 gl2 = drawable.getGL().getGL2();

		// Load shaders
		shaderProgID = myShaders.readAndCompile(gl2, "vshader.glsl",
				"fshader.glsl");
		if (shaderProgID == 0) {
			System.err.println("Error setting up shaders");
			System.exit(1);
		}

		// Other GL initialization....best to keep these
		gl2.glEnable(GL.GL_DEPTH_TEST);
		gl2.glEnable(GL.GL_CULL_FACE);
		gl2.glFrontFace(GL.GL_CCW);
		gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl2.glDepthFunc(GL.GL_LEQUAL);
		gl2.glClearDepth(1.0f);

		// define the geometry for your shapes
		createShapes();

	}

	private void createShapes() {
		makeCylinder();
		myCanvas.display();
	}
	
	private void makeCylinder() {
		float radius = 0.5f;
		float X1 = (float)0.5;
		float z1 = 0;

		float radDivs = (float)((float)3.14 * 2)/((float)10);
		float heightDivs = (float)((float)1.0/((float)8));
		float temp1 = (float)(radDivs/(float)2.0);
		float temp2 = (float)(heightDivs/(float)2.0);

		for (float rad = radDivs; rad < (((float)3.14 * 2) - temp1); rad += radDivs) {

			float x = (float)(Math.cos(rad) * radius);
			float z = (float)(Math.sin(rad) * radius);

			for (float y = (float)-0.5; y < (0.5 - temp2); y += heightDivs) {
				addTriangle(x, y, z, X1, y, z1, X1, y + heightDivs, z1);
				addTriangle(x, y, z, X1, y+heightDivs, z1, x, y+heightDivs, z);
			}

			addTriangle(0, (float)0.5, 0, x, (float)0.5, z, X1, (float)0.5, z1);
			addTriangle(0, (float)-0.5, 0, X1, (float)-0.5, z1, x, (float)-0.5, z);

			X1 = x;
			z1 = z;
		}

		for (float y = (float)-0.5; y < (0.5 - temp2); y += heightDivs) {
			addTriangle((float)0.5, y, 0, X1, y, z1, X1, y+heightDivs, z1);
			addTriangle((float)0.5, y, 0, X1, y+heightDivs, z1, (float)0.5, y+heightDivs, 0);
		}

		addTriangle(0, (float)0.5, 0, (float)0.5, (float)0.5, 0, X1, (float)0.5, z1);
		addTriangle(0, (float)-0.5, 0, X1, (float)-0.5, z1, (float)0.5, (float)-0.5, 0);
	}

	private void updateShape(int shape, GL2 gl2) {
		if (updateNeeded) {
			Buffer points = getVerticies();
			Buffer elements = getElements();

			// set up the vertex buffer
			int bf[] = new int[1];
			if (bufferInit) {
				bf[0] = vbuffer[shape];
				gl2.glDeleteBuffers(1, bf, 0);
			}
			gl2.glGenBuffers(1, bf, 0);
			vbuffer[shape] = bf[0];
			long vertBsize = getNVerts() * 4l * 4l;
			gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, vbuffer[shape]);
			gl2.glBufferData(GL.GL_ARRAY_BUFFER, vertBsize, points,
					GL.GL_STATIC_DRAW);
			bufferInit = true;

			// set up element buffer.
			if (bufferInit) {
				bf[0] = ebuffer[shape];
				gl2.glDeleteBuffers(1, bf, 0);
			}
			gl2.glGenBuffers(1, bf, 0);
			ebuffer[shape] = bf[0];
			long eBuffSize = getNVerts() * 2l;
			gl2.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[shape]);
			gl2.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, eBuffSize, elements,
					GL.GL_STATIC_DRAW);

			// set up vertex arrays in shader
			int vPosition = gl2.glGetAttribLocation(shaderProgID, "vPosition");
			gl2.glEnableVertexAttribArray(vPosition);
			gl2.glVertexAttribPointer(vPosition, 4, GL.GL_FLOAT, false, 0, 0l);

		}

		// pass in your rotation
		theta = gl2.glGetUniformLocation(shaderProgID, "theta");
		gl2.glUniform3fv(theta, 1, angles, shape * 3);

		// pass in your translation
		trans = gl2.glGetUniformLocation(shaderProgID, "trans");
		gl2.glUniform3fv(trans, 3, transData, shape * 3);

		// pass in your scaling
		scale = gl2.glGetUniformLocation(shaderProgID, "scale");
		gl2.glUniform3fv(scale, 3, scaleData, shape * 3);

		// draw your shapes
		gl2.glUseProgram(shaderProgID);
		int nElems = getNVerts();
		gl2.glDrawElements(GL.GL_TRIANGLES, nElems, GL.GL_UNSIGNED_SHORT, 0l);
		updateNeeded = false;

	}

	/**
	 * Called by the drawable during the first repaint after the component has
	 * been resized.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

	}

	/**
	 * Because I am a Key Listener...we'll only respond to key presses
	 */
	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Invoked when a key has been pressed.
	 */
	public void keyPressed(KeyEvent e) {
		// Get the key that was pressed
		char key = e.getKeyChar();

		// Respond appropriately
		switch (key) {
		case 'q':
		case 'Q':
			System.exit(0);
			break;
		}

		// do a redraw
		myCanvas.display();
	}

	/**
	 * main program
	 */
	public static void main(String[] args) {
		// GL setup
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);

		// create your tessMain
		openGLMain myMain = new openGLMain(canvas);

		Frame frame = new Frame("CG1 - OpenGL scene");
		frame.setSize(512, 512);
		frame.add(canvas);
		frame.setVisible(true);

		// by default, an AWT Frame doesn't do anything when you click
		// the close button; this bit of code will terminate the program when
		// the window is asked to close
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
