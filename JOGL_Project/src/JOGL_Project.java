import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Filename: JOGL_Project
 * Author: William Crutchfield
 * Date: 9/17/2017
 * Description: Draws multiple Objects into the scene using the data from the Object3D class.
 */
public class JOGL_Project extends GLJPanel implements GLEventListener, KeyListener {

  // Default Transform
  private double rotateX = 30; // Rotation along the X axis
  private double rotateY = -30; // Rotation along the Y axis
  private double rotateZ = 0; // Rotation along the Z axis
  private double translateX = 0; // Translation along the X axis
  private double translateY = 0; // Translation along the Y axis
  private double translateZ = 0; // Translation along the Z axis
  private double scale = 1.2; // Scale, or size, of the Object

  /** Constructor for JOGL_Project */
  private JOGL_Project() {
    super(new GLCapabilities(null));
    setPreferredSize(new Dimension(640, 480));
    addGLEventListener(this);
    addKeyListener(this);
  }

  public static void main(String[] args) {
    JFrame window =
        new JFrame(
            "Gems Galore | WASD -> Translate | +,- -> Zoom | Arrows -> Rotate | HOME -> Reset");
    JOGL_Project panel = new JOGL_Project();
    window.setContentPane(panel);
    window.pack();
    window.setLocation(50, 50);
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    window.setVisible(true);
    panel.requestFocusInWindow();
  }

  /**
   * Takes an object from Object3D class, then draws, and colors, said Object into the scene.
   *
   * @param gl2 Graphics interface
   * @param obj Object from Object3D class
   * @param scale Scale, or size, of the Object
   * @param x Translation along the X axis
   * @param y Translation along the Y axis
   * @param z Translation along the Z axis
   */
  private void drawObj(GL2 gl2, Object3D obj, double scale, double x, double y, double z) {
    gl2.glPushMatrix();
    gl2.glScaled(scale, scale, scale);  // scale unit to desired size
    gl2.glTranslated(x, y, z);          // translate unit to desired pos

    for (int i = 0; i < obj.faces.length; i++) {
      gl2.glPushMatrix();
      double r = obj.rgb[i][0];
      double g = obj.rgb[i][1];
      double b = obj.rgb[i][2];

      // Draws obj faces with appropriate color
      gl2.glColor3d(r, g, b);
      gl2.glBegin(GL2.GL_TRIANGLE_FAN);
      for (int j = 0; j < obj.faces[i].length; j++) {
        int v = obj.faces[i][j];
        gl2.glVertex3dv(obj.vertices[v], 0);
      }
      gl2.glEnd();

      // Draws obj outlines
      gl2.glColor3d(0, 0, 0);
      gl2.glBegin(GL2.GL_LINE_LOOP);
      for (int j = 0; j < obj.faces[i].length; j++) {
        int v = obj.faces[i][j];
        gl2.glVertex3dv(obj.vertices[v], 0);
      }
      gl2.glEnd();
      gl2.glPopMatrix();
    }
    gl2.glPopMatrix();
  }

  /** The display method is for when the panel needs to be redrawn. */
  public void display(GLAutoDrawable drawable) {

    GL2 gl2 = drawable.getGL().getGL2(); // The object that contains all the OpenGL methods.

    gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    gl2.glLoadIdentity();
    gl2.glRotated(rotateZ, 0, 0, 1);            // rotate Z of scene
    gl2.glRotated(rotateY, 0, 1, 0);            // rotate Y of scene
    gl2.glRotated(rotateX, 1, 0, 0);            // rotate X of scene
    gl2.glScaled(scale, scale, scale);                      // scales scene
    gl2.glTranslated(translateX, translateY, translateZ);   // translates scene

    // Draw Objects
    drawObj(gl2, Object3D.ground, 0.5, 0, -0.05, 0);
    drawObj(gl2, Object3D.gemGreen, 0.125, 0, 1.5, 0);
    drawObj(gl2, Object3D.gemBlue, 0.125, 1.5, 1.5, 1.5);
    drawObj(gl2, Object3D.gemRed, 0.125, -1.5, 1.5, -1.5);
    drawObj(gl2, Object3D.gemYellow, 0.125, 1.5, 1.5, -1.5);
    drawObj(gl2, Object3D.gemPurple, 0.125, -1.5, 1.5, 1.5);
  }

  /**
   * GLEventListener interface method, called when the window is initialized
   * @param drawable display for rendering
   */
  public void init(GLAutoDrawable drawable) {
    // called when the panel is created
    GL2 gl2 = drawable.getGL().getGL2();
    gl2.glMatrixMode(GL2.GL_PROJECTION);
    gl2.glOrtho(-1, 1, -1, 1, -1, 1);
    gl2.glMatrixMode(GL2.GL_MODELVIEW);
    gl2.glClearColor(0, 0, 0, 1);
    gl2.glEnable(GL2.GL_DEPTH_TEST);
    gl2.glLineWidth(2);
  }

  /**
   * GLEventListener interface method, called when the window is disposed
   * @param drawable display for rendering
   */
  public void dispose(GLAutoDrawable drawable) {}

  /**
   * GLEventListener interface method, called when the window is resized by the user
   * @param drawable display for rendering
   * @param x X position of the window
   * @param y Y position of the window
   * @param width Width of the window
   * @param height Height of the window
   */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}

  /**
   * KeyListener interface method, is called when a key is pressed
   * @param evt key pressed
   */
  public void keyPressed(KeyEvent evt) {
    int key = evt.getKeyCode();
    if (key == KeyEvent.VK_LEFT) rotateY -= 15;
    else if (key == KeyEvent.VK_RIGHT) rotateY += 15;
    else if (key == KeyEvent.VK_DOWN) rotateX -= 15;
    else if (key == KeyEvent.VK_UP) rotateX += 15;
    else if (key == KeyEvent.VK_PLUS || key == KeyEvent.VK_EQUALS) scale += .1;
    else if (key == KeyEvent.VK_MINUS) scale -= .1;
    else if (key == KeyEvent.VK_W) translateY += .1;
    else if (key == KeyEvent.VK_A) translateX -= .1;
    else if (key == KeyEvent.VK_S) translateY -= .1;
    else if (key == KeyEvent.VK_D) translateX += .1;
    else if (key == KeyEvent.VK_HOME) {
      rotateX = 30;
      rotateY = -30;
      rotateZ = translateX = translateY = translateZ = 0;
      scale = 1.2;
    }
    repaint();
  }

  /**
   * KeyListener interface method, is called when a key is released
   * @param evt key released
   */
  public void keyReleased(KeyEvent evt) {}

  /**
   * KeyListener interface method, is called when a key is typed
   * @param evt key typed
   */
  public void keyTyped(KeyEvent evt) {}
}
