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
import java.io.InputStream;
import java.nio.Buffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class openGLMain extends cg1Shape implements GLEventListener, KeyListener
{	
	private boolean bufferInit = false;
	private int vbuffer;
	private int ebuffer;

	public float angles[];
	public float ambientPoints[] = new float[4];
	public float diffusePoints[] = new float[4];
	public float specularPoints[] = new float[4];
	public float shininessPoints[] = new float[1];;
	public float lightposPoints[] = new float[4];
	public float eyepointPoints[] = new float[3];
	public float lookatPoints[] = new float[3];
	public float upPoints[] = new float[3];

	private shaderProgram myShaders;
	private int shaderProgID = 0;
	public int theta;
	private boolean updateNeeded = true;

	/**
	 * my canvas
	 */
	GLCanvas myCanvas;

	/**
	 * constructor
	 */
	public openGLMain(GLCanvas G)
	{
		angles = new float[3];
		angles[0] = 125.0f;
		angles[1] = 120.0f;
		angles[2] = 125.0f;

		ambientPoints[0] = 0.1f;
		ambientPoints[1] = 0.2f;
		ambientPoints[2] = 0.3f;
		ambientPoints[3] = 1.0f;

		diffusePoints[0] = 0.4f;
		diffusePoints[1] = 0.5f;
		diffusePoints[2] = 0.6f;
		diffusePoints[3] = 1.0f;

		specularPoints[0] = 0.7f;
		specularPoints[1] = 0.8f;
		specularPoints[2] = 0.9f;
		specularPoints[3] = 1.0f;

		shininessPoints[0] = 30.0f;
		
		lightposPoints[0] = 2.0f;
		lightposPoints[1] = 2.0f;
		lightposPoints[2] = 0.0f;
		lightposPoints[3] = 1.0f;

		eyepointPoints[0] = 1.0f;
		eyepointPoints[1] = -1.0f;
		eyepointPoints[2] = 2.0f;

		lookatPoints[0] = 1.5f;
		lookatPoints[1] = 0.1f;
		lookatPoints[2] = 0.1f;

		upPoints[0] = 0.0f;
		upPoints[1] = 1.0f;
		upPoints[2] = 0.0f;

		myShaders = new shaderProgram();
		myCanvas = G;

		G.addGLEventListener (this);
		G.addKeyListener (this);
	}

	private void errorCheck (GL2 gl2)
	{
		int code = gl2.glGetError();
		if (code == GL.GL_NO_ERROR) 
			System.err.println ("All is well");
		else
			System.err.println ("Problem - error code : " + code);

	}

	/**
	 * Called by the drawable to initiate OpenGL rendering by the client. 
	 */
	public void display(GLAutoDrawable drawable)
	{
		GL2 gl2 = (drawable.getGL()).getGL2();
		gl2.glClear ( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl2.glCullFace ( GL.GL_BACK );
		
		try{
            TextureData textureData = TextureIO.newTextureData(GLProfile.getDefault(), getClass().getResourceAsStream("cg.jpeg"), false, "jpeg");
            Texture texture = TextureIO.newTexture(textureData);
            gl2.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            gl2.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl2.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl2.glUseProgram (shaderProgID);
            texture.bind(gl2);
		}
        catch(Exception E)
		{
		}
		updateShape(0, gl2);
	}       

	/**
	 * updates the shape, set values to shaders
	 * 
	 * @param shape
	 * @param gl2
	 */
	private void updateShape(int shape, GL2 gl2) {
		if (updateNeeded=true) {
			// get your verticies and elements
			Buffer points = getVerticies();
			Buffer normals = getNormals();
			Buffer textures = getTextures();
			Buffer elements = getElements();

			// set up the vertex buffer
			int bf[] = new int[1];
			if (bufferInit) {
				bf[0] = vbuffer;
				gl2.glDeleteBuffers(1, bf, 0);
			}
			gl2.glGenBuffers (1, bf, 0);
			vbuffer = bf[0];
			long vertBsize = getNVerts() * 4l * 4l;
			long normSize = getNVerts() * 3l * 4l;
			long textsize = getNVerts() * 2l * 4l;
			gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer);
			gl2.glBufferData ( GL.GL_ARRAY_BUFFER, vertBsize + normSize + textsize, null, GL.GL_STATIC_DRAW);

			gl2.glBufferSubData(GL.GL_ARRAY_BUFFER,0,vertBsize, points);
			gl2.glBufferSubData(GL.GL_ARRAY_BUFFER, vertBsize, normSize, normals);
			gl2.glBufferSubData(GL.GL_ARRAY_BUFFER, vertBsize + normSize, textsize, textures);
			bufferInit = true;

			// set up element buffer.
			if (bufferInit) {
				bf[0] = ebuffer;
				gl2.glDeleteBuffers(1, bf, 0);
			}
			gl2.glGenBuffers (1, bf, 0);
			ebuffer = bf[0];
			long eBuffSize = getNVerts() * 2l;
			gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer);
			gl2.glBufferData ( GL.GL_ELEMENT_ARRAY_BUFFER, eBuffSize,elements, 
					GL.GL_STATIC_DRAW);                   

			// set up vertex arrays in shader
			int  vPosition = gl2.glGetAttribLocation (shaderProgID, "vPosition");
			gl2.glEnableVertexAttribArray ( vPosition );
			gl2.glVertexAttribPointer (vPosition, 4, GL.GL_FLOAT, false,
                    0, 0l);
			
			int  vNormal = gl2.glGetAttribLocation (shaderProgID, "vNormal");
			gl2.glEnableVertexAttribArray ( vNormal );
			gl2.glVertexAttribPointer (vNormal, 3, GL.GL_FLOAT, false, 0, vertBsize);
			
			int  vTexCoord = gl2.glGetAttribLocation (shaderProgID, "vTexCoord");
            gl2.glEnableVertexAttribArray ( vTexCoord );
            gl2.glVertexAttribPointer (vTexCoord, 2, GL.GL_FLOAT, false,
                                    0, vertBsize + normSize);

			int AmbientProduct = gl2.glGetUniformLocation (shaderProgID, "AmbientProduct");
			gl2.glUniform4fv (AmbientProduct, 1, ambientPoints, 0);

			int DiffuseProduct = gl2.glGetUniformLocation (shaderProgID, "DiffuseProduct");
			gl2.glUniform4fv (DiffuseProduct, 1, diffusePoints, 0);

			int SpecularProduct = gl2.glGetUniformLocation (shaderProgID, "SpecularProduct");
			gl2.glUniform4fv (SpecularProduct, 1, specularPoints, 0);

			int LightPosition = gl2.glGetUniformLocation (shaderProgID, "LightPosition");
			gl2.glUniform4fv (LightPosition, 1, lightposPoints, 0);

			int Shininess = gl2.glGetUniformLocation (shaderProgID, "Shininess");
			gl2.glUniform1fv (Shininess, 1, shininessPoints, 0);

			int eyePoint = gl2.glGetUniformLocation (shaderProgID, "eyePoint");
			gl2.glUniform3fv (eyePoint, 1, eyepointPoints, 0);

			int lookAt = gl2.glGetUniformLocation (shaderProgID, "lookAt");
			gl2.glUniform3fv (lookAt, 1, lookatPoints, 0);

			int up = gl2.glGetUniformLocation (shaderProgID, "up");
			gl2.glUniform3fv (up, 1, upPoints, 0);

			// pass in your rotation
			theta = gl2.glGetUniformLocation (shaderProgID, "theta");
			gl2.glUniform3fv (theta, 1, angles, 0);

			// draw your shapes
		}

		gl2.glUseProgram (shaderProgID);
		int nElems = getNVerts();
		gl2.glDrawElements ( GL.GL_TRIANGLES, nElems,  GL.GL_UNSIGNED_SHORT, 0l);

	}

	/**
	 * Notifies the listener to perform the release of all OpenGL 
	 * resources per GLContext, such as memory buffers and GLSL 
	 * programs.
	 */
	public void dispose(GLAutoDrawable drawable)
	{

	}

	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized. 
	 */
	public void init(GLAutoDrawable drawable)
	{
		// get the gl object
		GL2 gl2 = drawable.getGL().getGL2();

		// Load shaders
		shaderProgID = myShaders.readAndCompile (gl2, "vshader.glsl", "fshader.glsl");
		if (shaderProgID == 0) {
			System.err.println ("Error setting up shaders");
			System.exit (1);
		}

		// Other GL initialization....best to keep these
		gl2.glEnable (GL.GL_DEPTH_TEST);
		gl2.glEnable (GL.GL_CULL_FACE);
		gl2.glFrontFace(GL.GL_CCW);
		gl2.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
		gl2.glDepthFunc (GL.GL_LEQUAL);
		gl2.glClearDepth (1.0f);

		// define the geometry for yor shapes
		createShapes();

	}

	/**
	 * Called by the drawable during the first repaint after the component
	 * has been resized. 
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height)
	{

	}

	/**
	 * creates a new shape
	 */
	public void createShapes()
	{
		makeCylinder(); 
		updateNeeded = true;
	}

	/**
	 * Because I am a Key Listener...we'll only respond to key presses
	 */
	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}

	/** 
	 * Invoked when a key has been pressed.
	 */
	public void keyPressed(KeyEvent e)
	{
		// Get the key that was pressed
		char key = e.getKeyChar();

		// Respond appropriately
		switch( key ) {
		case 'q': case 'Q':
			System.exit( 0 );
			break;
		}

		// do a redraw
		myCanvas.display();
	}


	/**
	 * main program
	 */
	public static void main(String [] args)
	{
		// GL setup
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);

		// create your tessMain
		openGLMain myMain = new openGLMain(canvas);
		//	myMain.updateNeeded=true;
		//   myMain.createNewShape();

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
