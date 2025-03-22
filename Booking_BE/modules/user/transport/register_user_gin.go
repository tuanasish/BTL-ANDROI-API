// transport/register.go
package transport

import (
	"BOOKING_BE/common"
	"BOOKING_BE/modules/user/biz"
	"BOOKING_BE/modules/user/model"
	"BOOKING_BE/modules/user/storage"

	"log"
	"net/http"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

// API Đăng ký
func Register(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		log.Println("Register API called") // Log API

		var user model.UserCreate
		if err := c.ShouldBindJSON(&user); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid request", "details": err.Error()})
			return
		}

		store := storage.NewUserStorage(db)
		business := biz.NewRegisterUserBiz(store)

		log.Println("Calling RegisterUser") // Log trước khi gọi RegisterUser
		if err := business.RegisterUser(c.Request.Context(), &user); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		log.Println("User registered successfully") // Log khi đăng ký thành công
		c.JSON(http.StatusOK, gin.H{"message": common.SimpleSuccessResponse(user)})
	}
}
