all: SimpleRender.class

SimpleRender.class: SimpleRender.java Playground.class
	javac SimpleRender.java

Playground.class: Playground.java Vector.class Ball.class Paddle.class Brick.class Powerup.class Announcement.class
	javac Playground.java

Paddle.class: Paddle.java Rectangle.class Vector.class
	javac Paddle.java

Brick.class: Brick.java Rectangle.class Vector.class
	javac Brick.java

Rectangle.class: Rectangle.java Sprite.class Vector.class
	javac Rectangle.java

Ball.class: Ball.java Circle.class Vector.class
	javac Ball.java

Powerup.class: Powerup.java Circle.class Vector.class
	javac Powerup.java

Circle.class: Circle.java Sprite.class Vector.class
	javac Circle.java

Sprite.class: Sprite.java Vector.class
	javac Sprite.java

Vector.class: Vector.java
	javac Vector.java

Announcement.class: Announcement.java
	javac Announcement.java

clean: 
	rm *.class

run: SimpleRender.class
	java SimpleRender