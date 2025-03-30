package transport

import (
	"BOOKING_BE/modules/user/storage"
	

	"log"
	"net/http"

	"github.com/gin-gonic/gin"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

// API Đăng nhập
// func Login(db *gorm.DB) func(*gin.Context) {
// 	return func(c *gin.Context) {
// 		// Lấy email và password từ query params
// 		email := c.Query("email")
// 		pwd := c.Query("password")

// 		// Kiểm tra dữ liệu đầu vào
// 		if email == "" || pwd == "" {
// 			c.JSON(http.StatusBadRequest, gin.H{"error": "Thiếu email hoặc password"})
// 			return
// 		}

// 		// Khởi tạo storage và business logic

// 		store := storage.NewUserStorage(db)
// 		biz := biz.NewLoginUserBiz(store)

// 		// Kiểm tra đăng nhập
// 		if err := biz.CheckLogin(email, pwd); err != nil {
// 			c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
// 			return
// 		}

//			// Trả về kết quả thành công
//			// Trả về thông tin user
//			c.JSON(http.StatusOK, gin.H{
//				"message": "Đăng nhập thành công",
//				"user": gin.H{
//					"user_id": user.ID,
//					"username": user.Username,
//					"email": user.Email,
//					"phone": user.Phone,
//					"role": user.Role,
//				},
//				"token": "abc123xyz", // Nếu có JWT thì tạo token ở đây
//			})
//		}
//	}
func Login(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		email := c.Query("email")
		password := c.Query("password")

		// Debug: In ra thông tin nhận được
		log.Printf("Attempting login - Email: %s, Password: %s", email, password)

		store := storage.NewUserStorage(db)

		// 1. Tìm user bằng email
		user, err := store.FindUserByEmail(email)
		if err != nil {
			log.Printf("User not found for email %s: %v", email, err)
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Email hoặc mật khẩu không đúng"})
			return
		}

		// Debug: In ra thông tin user tìm được
		log.Printf("Found user: %+v", user)

		// 2. So sánh mật khẩu
		err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(password))
		if err != nil {
			log.Printf("Password mismatch for user %s: %v", email, err)
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Email hoặc mật khẩu không đúng"})
			return
		}

		// 3. Trả về thành công
		log.Printf("Login successful for user %s", email)
		c.JSON(http.StatusOK, gin.H{
			"message": "Đăng nhập thành công",
			"user": gin.H{
				"user_id":  user.ID,
				"username": user.Username,
				"email":    user.Email,
				"phone":    user.Phone,
				"role":     user.Role,
			},
		})
	}
}
