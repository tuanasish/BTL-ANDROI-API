// biz/register.go
package biz

import (
	"BOOKING_BE/modules/user/model"
	"context"
	"log"

	"golang.org/x/crypto/bcrypt"
)

// Interface cho storage, đảm bảo có thể lưu user vào database
type RegisterUser interface {
	CreateUser(cxt context.Context, data *model.UserCreate) error
}

// Business Logic xử lý đăng ký người dùng
type RegisterUserBiz struct {
	store RegisterUser
}

// Constructor đúng cách
func NewRegisterUserBiz(store RegisterUser) *RegisterUserBiz {
	return &RegisterUserBiz{store: store}
}

// Mã hóa mật khẩu với bcrypt
func hashPassword(password string) (string, error) {
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	if err != nil {
		return "", err
	}
	return string(hashedPassword), nil
}

// Đăng ký người dùng mới
func (biz *RegisterUserBiz) RegisterUser(ctx context.Context, data *model.UserCreate) error {
	log.Println("RegisterUser is called")

	// hashedPassword, err := hashPassword(data.Password)
	// if err != nil {
	// 	log.Println("Error hashing password:", err)
	// 	return err
	// }
	// data.Password = hashedPassword

	log.Println("Calling CreateUser") // Log trước khi gọi CreateUser
	if err := biz.store.CreateUser(ctx, data); err != nil {
		log.Println("Error creating user:", err)
		return err
	}

	log.Println("User created successfully")
	return nil
}
