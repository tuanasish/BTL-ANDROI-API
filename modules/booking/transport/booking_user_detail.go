// transport/user_bookings_handler.go
package transport

import (
	"BOOKING_BE/common"
	"BOOKING_BE/modules/booking/biz"
	"BOOKING_BE/modules/booking/storage"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

// GetUserBookings là handler lấy danh sách đặt sân của user
func GetUserBookings(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		// Lấy user_id từ URL parameter
		userIDParam := c.Param("user_id")
		userID, err := strconv.Atoi(userIDParam)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "ID không hợp lệ"})
			return
		}

		// Khởi tạo storage và biz
		store := storage.NewBookingStorage(db)
		bizLayer := biz.NewGetUserBookingsBiz(store)

		// Gọi biz để lấy danh sách booking của user
		bookings, err := bizLayer.GetBookingsByUser(c, int(userID))
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Không thể lấy danh sách booking"})
			return
		}

		// Trả về dữ liệu thành công
		c.JSON(http.StatusOK, gin.H{
			"success": common.SimpleSuccessResponse(bookings),
			"data":    bookings,
		})
	}
}
