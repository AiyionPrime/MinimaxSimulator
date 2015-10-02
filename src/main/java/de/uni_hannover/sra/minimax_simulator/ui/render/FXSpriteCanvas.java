package de.uni_hannover.sra.minimax_simulator.ui.render;

import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

// TODO: full JavaFX update
public class FXSpriteCanvas<T> extends Canvas {

	private final Map<T, Sprite> _sprites;

	private RenderEnvironment _env;
	private SpriteFactory _spriteFactory;

	private final GraphicsContext gc;

	public FXSpriteCanvas()
	{
		_sprites = new HashMap<T, Sprite>();
		gc = this.getGraphicsContext2D();
	}

	protected void draw() {
		if (_env == null) {
			throw new IllegalStateException("Cannot render SpriteCanvas without RenderEnvironment set");
		}
		gc.setFont(_env.getFontFX());
		gc.clearRect(0, 0, getWidth(), getHeight());

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, getWidth(), getHeight());

		drawBorder();

		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);

		for (Sprite sprite : _sprites.values()) {
			sprite.paint(gc, _env);
		}
	}

	/**
	 * Draws a thin border around the {@code FXSpriteCanvas}.
	 */
	private void drawBorder() {
		double maxY = getHeight();
		double maxX = getWidth();

		gc.save();
		gc.setLineWidth(1);

		gc.beginPath();
		gc.moveTo(0, 0);
		gc.lineTo(0, maxY);
		gc.lineTo(maxX, maxY);
		gc.lineTo(maxX, 0);
		gc.lineTo(0, 0);
		gc.closePath();
		gc.stroke();

		gc.restore();
	}

	public void setSprite(T owner, Sprite sprite)
	{
		checkNotNull(owner, "Sprite owner must not be null");
		checkNotNull(sprite, "Sprite must not be null");

		_sprites.put(owner, sprite);
		draw();
	}

	public void setSprite(T owner)
	{
		checkNotNull(owner, "Sprite owner must not be null");
		checkState(_spriteFactory != null, "Must provide a sprite or set a SpriteFactory");

		Sprite sprite = _spriteFactory.createSprite(owner);
		setSprite(owner, sprite);
	}

	public void removeSprite(T owner)
	{
		if (_sprites.remove(owner) != null)
			draw();
	}

	public SpriteFactory getSpriteFactory()
	{
		return _spriteFactory;
	}

	public void setSpriteFactory(SpriteFactory spriteFactory)
	{
		_spriteFactory = spriteFactory;
	}

	public RenderEnvironment getRenderEnvironment()
	{
		return _env;
	}

	public void setEnvironment(RenderEnvironment env)
	{
		_env = env;
	}

	public com.sun.javafx.tk.FontMetrics getFontMetrics(javafx.scene.text.Font font) {
		return com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	}

	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
}