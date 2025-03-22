package storage

import (
	"BOOKING_BE/modules/user/model"
	"context"
	"errors"
	"log"

	"gorm.io/gorm"
)

var (
	ErrUserNotFound  = errors.New("user not found")
	ErrWrongPassword = errors.New("incorrect password")
)

type UserStorage struct {
	db *gorm.DB
}

func NewUserStorage(db *gorm.DB) *UserStorage {
	return &UserStorage{db: db}
}

// Hàm tạo user
func (s *UserStorage) CreateUser(cxt context.Context, user *model.UserCreate) error {
	log.Println("CreateUser is called") // Log để debug
	if s.db == nil {
		return errors.New("database connection is nil")
	}
	return s.db.Create(user).Error
}

func (s *UserStorage) GetUserByEmailPwd(email, pwd string) error {
	var user model.User

	// Lấy dữ liệu từ database
	result := s.db.Where("email = ?", email).First(&user)
	if result.Error != nil { // Phải lấy lỗi từ result.Error
		if errors.Is(result.Error, gorm.ErrRecordNotFound) {
			return ErrUserNotFound
		}
		return result.Error
	}

	// Kiểm tra mật khẩu
	if user.Password != pwd {
		return ErrWrongPassword
	}

	return nil
}
