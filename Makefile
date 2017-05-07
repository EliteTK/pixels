TARGET = pixels.jar
CLASS := window/Window.class window/DrawingPlane.class \
	 window/DrawingPlaneSupersampled.class \
	 graphics/mandelbrot/MandelbrotColourtable.class \
	 graphics/mandelbrot/MandelbrotColourtableFunction.class \
	 graphics/mandelbrot/Mandelbrot.class pos/Posxy.class \
	 start/KeyboardEventHandler.class start/Stalk2.class \
	 start/ColourFunc.class start/Argument.class start/Main.class

all: $(TARGET)
$(TARGET): $(CLASS)
	jar cvfe $@ start.Main $^

%.class: %.java
	javac $<

clean:
	$(RM) $(CLASS) $(TARGET)
