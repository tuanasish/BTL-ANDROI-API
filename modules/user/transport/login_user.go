package transport

import (
	"BOOKING_BE/modules/user/biz"
	"BOOKING_BE/modules/user/storage"
	"net/http"

	"github.com/gin-gonic/gin"
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

// 		// Trả về kết quả thành công
// 		// Trả về thông tin user
// 		c.JSON(http.StatusOK, gin.H{
// 			"message": "Đăng nhập thành công",
// 			"user": gin.H{
// 				"user_id": user.ID,
// 				"username": user.Username,
// 				"email": user.Email,
// 				"phone": user.Phone,
// 				"role": user.Role,
// 			},
// 			"token": "abc123xyz", // Nếu có JWT thì tạo token ở đây
// 		})
// 	}
// }
func Login(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		// Lấy email và password từ query params
		email := c.Query("email")
		pwd := c.Query("password")

		// Kiểm tra dữ liệu đầu vào
		if email == "" || pwd == "" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Thiếu email hoặc password"})
			return
		}

		// Khởi tạo storage và business logic
		store := storage.NewUserStorage(db)
		biz := biz.NewLoginUserBiz(store)

		// Kiểm tra đăng nhập
		if err := biz.CheckLogin(email, pwd); err != nil {
			c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
			return
		}

		// Tìm user sau khi kiểm tra đăng nhập
		user, err := store.FindUserByEmail(email)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Không tìm thấy user"})
			return
		}

		// Trả về kết quả thành công
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

