package transport

import (
	"BOOKING_BE/modules/booking/biz"
	"BOOKING_BE/modules/booking/model"
	"BOOKING_BE/modules/booking/storage"
	"context"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func UpdateBooking(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		// Lấy booking_id từ URL
		bookingIDParam := c.Param("booking_id")
		bookingID, err := strconv.Atoi(bookingIDParam)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "ID không hợp lệ"})
			return
		}

		// Parse JSON request
		var updateData model.UpdateBooking
		if err := c.ShouldBindJSON(&updateData); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Dữ liệu không hợp lệ"})
			return
		}

		// Khởi tạo storage & biz
		store := storage.NewUpdateBookingStorage(db)
		bizLayer := biz.NewUpdateBookingBiz(store)

		// Gọi business logic
		if err := bizLayer.UpdateBookingBiz(context.Background(), int(bookingID), &updateData); err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		// Trả về kết quả thành công
		c.JSON(http.StatusOK, gin.H{"success": true, "message": "Cập nhật thành công"})
	}
}
