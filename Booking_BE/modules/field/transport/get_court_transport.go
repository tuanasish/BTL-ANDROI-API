package transport

import (
	"BOOKING_BE/modules/field/biz"
	"BOOKING_BE/modules/field/storage"
	"context"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

// GetCourtsHandler trả về số lượng court và danh sách id của các court của một field
func GetCourtsHandler(db *gorm.DB) func(*gin.Context) {
	return func(c *gin.Context) {
		// Lấy field_id từ URL parameter
		fieldIDStr := c.Param("id")
		fieldIDInt, err := strconv.Atoi(fieldIDStr)
		if err != nil || fieldIDInt <= 0 {
			c.JSON(http.StatusBadRequest, gin.H{"error": "field id không hợp lệ"})
			return
		}
		fieldID := int(fieldIDInt)

		// Khởi tạo storage và biz
		fieldStorage := storage.NewStorageField(db)
		getCourtBiz := biz.NewGetCourtListBiz(fieldStorage)

		// Gọi business logic để lấy danh sách court
		count, courtIDs, err := getCourtBiz.GetCourts(context.Background(), fieldID)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		// Trả về dữ liệu
		c.JSON(http.StatusOK, gin.H{
			"court_count": count,
			"court_ids":   courtIDs,
		})
	}
}
