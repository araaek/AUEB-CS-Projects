#include "player.h"
#include "util.h"
#include <cmath>

int Player::getHealth() const
{
	return m_health;
}

int Player::getScore() const
{
	return m_score;
}

void Player::update(float dt)
{
	float delta_time = dt / 1000.0f;

	movePlayer(dt);

	// update offset for other game objects
	m_state->m_global_offset_x = m_state->getCanvasWidth() / 2.0f - m_pos_x;
	m_state->m_global_offset_y = m_state->getCanvasHeight() / 2.0f - m_pos_y;
	GameObject::update(dt);
}

void Player::draw()
{
	int sprite = (int)fmod(100.0f - m_pos_x * 9.0f, m_sprites.size());

	m_brush_player.texture = m_sprites[sprite];
	graphics::drawRect(m_state->getCanvasWidth() * 0.5f, m_state->getCanvasHeight() * 0.5f, 1.0f, 1.0f, m_brush_player);

	if (m_state->m_debugging)
		debugDraw();

	// Δείχνουμε το σκόρ
	graphics::Brush br;
	graphics::setFont(m_state->getFullAssetPath("frosty.ttf"));
	SETCOLOR(br.fill_color, 0, 0, 1);
	SETCOLOR(br.outline_color, 0, 0, 0);
	br.fill_opacity = 0.8f;
	br.outline_opacity = 1.0f;

	std::string scoreHealthText = "Score: " + std::to_string(m_score)+"   Health: "+std::to_string(m_health);
	graphics::drawText(m_state->getCanvasWidth() * 0.5f - 1.0f, m_state->getCanvasHeight() * 0.5f - 0.7f, 0.2f, scoreHealthText, br);
}

void Player::init()
{
	// stage 1
	m_pos_x = 5.0f;
	m_pos_y = 5.0f;

	m_state->m_global_offset_x = m_state->getCanvasWidth() / 2.0f - m_pos_x;
	m_state->m_global_offset_y = m_state->getCanvasHeight() / 2.0f - m_pos_y;

	m_brush_player.fill_opacity = 1.0f;
	m_brush_player.outline_opacity = 0.0f;
	m_brush_player.texture = m_state->getFullAssetPath("snowman_left_1.png");


	m_sprites.push_back(m_state->getFullAssetPath("snowman_left_1.png"));
	m_sprites.push_back(m_state->getFullAssetPath("snowman_left_2.png"));
	m_sprites.push_back(m_state->getFullAssetPath("snowman_right_1.png"));
	m_sprites.push_back(m_state->getFullAssetPath("snowman_right_2.png"));

	// Adjust width for finer collision detection
	m_width *= 0.6f;
}

void Player::debugDraw()
{
	graphics::Brush debug_brush;
	SETCOLOR(debug_brush.fill_color, 1, 0.3f, 0);
	SETCOLOR(debug_brush.outline_color, 1, 0.1f, 0);
	debug_brush.fill_opacity = 0.1f;
	debug_brush.outline_opacity = 1.0f;
	graphics::drawRect(m_state->getCanvasWidth() * 0.5f, m_state->getCanvasHeight() * 0.5f, m_width, m_height, debug_brush);

	char s[20];
	sprintf_s(s, "(%5.2f, %5.2f)", m_pos_x, m_pos_y);
	SETCOLOR(debug_brush.fill_color, 1, 0, 0);
	graphics::setFont(m_state->getFullAssetPath("OpenSans-Regular.ttf"));
	debug_brush.fill_opacity = 1.0f;
	graphics::drawText(m_state->getCanvasWidth() * 0.5f - 0.4f, m_state->getCanvasHeight() * 0.5f - 0.6f, 0.15f, s, debug_brush);
}

void Player::movePlayer(float dt)
{
	float delta_time = dt / 1000.0f;

	bool moved = false;

	// Stage 2 code: Acceleration-based velocity
	float move = 0.0f;
	if (graphics::getKeyState(graphics::SCANCODE_A)) {
		move -= 1.0f;
		moved = true; // Ο παίκτης κινήθηκε
	}
	if (graphics::getKeyState(graphics::SCANCODE_D)) {
		move += 1.0f;
		moved = true; // Ο παίκτης κινήθηκε
	}

	if (moved) {
		m_score += 1; // Αυξήστε το σκορ κατά 1 όταν ο παίκτης κινείται δεξιά ή αριστερά
	}

	m_vx = std::min<float>(m_max_velocity, m_vx + delta_time * move * m_accel_horizontal);
	m_vx = std::max<float>(-m_max_velocity, m_vx);

	// friction
	m_vx -= 0.2f * m_vx / (0.1f + fabs(m_vx));

	// apply static friction threshold
	if (fabs(m_vx) < 0.01f)
		m_vx = 0.0f;

	// adjust horizontal position
	m_pos_x += m_vx * delta_time;

	// jump only when not in flight:
	if (m_vy == 0.0f)
		m_vy -= (graphics::getKeyState(graphics::SCANCODE_W) ? m_accel_vertical : 0.0f) * 0.02f;// not delta_time!! Burst 

	// add gravity
	m_vy += delta_time * m_gravity;

	// adjust vertical position
	m_pos_y += m_vy * delta_time;


}

