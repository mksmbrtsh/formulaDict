package maximsblog.blogspot.com.formuladict;

import java.util.Locale;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import android.app.Application;

public class App extends Application {
	
	public static final int CUSTOM_FORMULS = 6;
	
	public static String getLanguageID(){
		String LanguageID;
		switch (Locale.getDefault().getLanguage()) {
		case "ru":
			LanguageID = "1";
			break;
		case "en":
			LanguageID = "2";
			break;
		case "de":
			LanguageID = "3";
			break;
		default:
			LanguageID = "2";
			break;
		}
		return LanguageID;
	}
	
	public static int getMaxTextureSize() {
	    // Safe minimum default size
	    final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

	    // Get EGL Display
	    EGL10 egl = (EGL10) EGLContext.getEGL();
	    EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

	    // Initialise
	    int[] version = new int[2];
	    egl.eglInitialize(display, version);

	    // Query total number of configurations
	    int[] totalConfigurations = new int[1];
	    egl.eglGetConfigs(display, null, 0, totalConfigurations);

	    // Query actual list configurations
	    EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
	    egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

	    int[] textureSize = new int[1];
	    int maximumTextureSize = 0;

	    // Iterate through all the configurations to located the maximum texture size
	    for (int i = 0; i < totalConfigurations[0]; i++) {
	        // Only need to check for width since opengl textures are always squared
	        egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

	        // Keep track of the maximum texture size
	        if (maximumTextureSize < textureSize[0])
	            maximumTextureSize = textureSize[0];
	    }

	    // Release
	    egl.eglTerminate(display);

	    // Return largest texture size found, or default
	    return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		AjLatexMath.init(this);
		AjLatexMath.foreground = getResources().getColor(R.color.formula_text);
	}
}
