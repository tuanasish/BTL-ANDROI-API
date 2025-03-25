package transport

import (
	"BOOKING_BE/common"
	"BOOKING_BE/modules/booking/biz"
	"BOOKING_BE/modules/booking/model"
	"BOOKING_BE/modules/booking/storage"
	"context"
	"net/http"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func CreateBooking(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		var data model.Booking // Không dùng con trỏ ở đây

		// Parse JSON request
		if err := c.ShouldBindJSON(&data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Không lấy được dữ liệu"})
			return
		}

		store := storage.NewBookingStorage(db)
		biz := biz.NewCreateBookingBiz(store)

		// Gọi business logic để tạo booking
		if err := biz.CreateBookingBiz(context.Background(), &data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, gin.H{"success": common.SimpleSuccessResponse(data)})
	}
}
