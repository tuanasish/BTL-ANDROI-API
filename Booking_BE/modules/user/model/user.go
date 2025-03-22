package model

import (
	"errors"
	"time"
)

var (
	ErrTitleEmpty = errors.New("title can not empty")
	ErrEmail      = errors.New("Email can not empty")
	ErrPWD        = errors.New("PassWord can not empty")
)

type User struct {
	ID        int        `json:"user_id" gorm:"column:user_id;primaryKey;autoIncrement"`
	Username  string     `json:"username" gorm:"column:username;not null"`
	Password  string     `json:"password" gorm:"column:password;not null"`
	Email     string     `json:"email" gorm:"column:email;not null"`
	Phone     string     `json:"phone" gorm:"column:phone"`
	Role      *RoleUser  `json:"role" gorm:"column:role"`
	CreatedAt *time.Time `json:"created_at" gorm:"column:created_at;autoCreateTime"`
	UpdatedAt *time.Time `json:"updated_at" gorm:"column:updated_at;autoUpdateTime"`
}

// Đảm bảo GORM dùng đúng tên bảng
func (User) TableName() string {
	return "Users"
}

type UserCreate struct {
	Username string    `json:"username" gorm:"column:username;not null"`
	Password string    `json:"password" gorm:"column:password;not null"`
	Email    string    `json:"email" gorm:"column:email;not null"`
	Phone    string    `json:"phone" gorm:"column:phone"`
	Role     *RoleUser `json:"role" gorm:"column:role"`
}

func (UserCreate) TableName() string {
	return User{}.TableName()
}

type UserUpdate struct {
	Username string    `json:"username" gorm:"column:username"`
	Password string    `json:"password" gorm:"column:password"`
	Email    string    `json:"email" gorm:"column:email"`
	Phone    string    `json:"phone" gorm:"column:phone"`
	Role     *RoleUser `json:"role" gorm:"column:role"`
}

func (UserUpdate) TableName() string {
	return User{}.TableName()
}
