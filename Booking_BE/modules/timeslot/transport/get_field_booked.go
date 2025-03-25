package transport

import (
	"BOOKING_BE/modules/timeslot/biz"
	"BOOKING_BE/modules/timeslot/storage"
	"context"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func GetBookedTimeSlotsByField(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Lấy field_id từ URL path, vd: /fields/:field_id/booked
		fieldID, err := strconv.Atoi(c.Param("field_id"))
		if err != nil || fieldID <= 0 {
			c.JSON(http.StatusBadRequest, gin.H{"error": "field_id không hợp lệ"})
			return
		}

		// Lấy ngày từ query parameter, vd: /fields/{field_id}/booked?date=2025-03-20
		date := c.Query("date")
		if date == "" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Ngày không hợp lệ"})
			return
		}

		// Khởi tạo storage và biz
		fieldStorage := storage.NewStorageField(db)
		slotBiz := biz.NewGetBookedTimeSlotsBiz(fieldStorage)

		// Lấy danh sách khung giờ đã đặt cho field cụ thể trong ngày
		slots, err := slotBiz.GetBookedTimeSlotsByField(context.Background(), fieldID, date)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		// Trả về kết quả dưới dạng JSON
		c.JSON(http.StatusOK, gin.H{"data": slots})
	}
}
