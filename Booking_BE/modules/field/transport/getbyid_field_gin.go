package transport

import (
	"BOOKING_BE/modules/field/biz"
	"BOOKING_BE/modules/field/storage"
	"context"
	"net/http"
	"strconv"
	"strings"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func GetFieldByID(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Lấy ID từ URL param
		id, err := strconv.Atoi(c.Param("id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "ID không hợp lệ"})
			return
		}

		// Khởi tạo storage và biz
		fieldStorage := storage.NewStorageField(db)
		fieldBiz := biz.NewGetFieldBiz(fieldStorage)

		// Gọi Biz để lấy thông tin sân
		field, err := fieldBiz.GetFieldByID(context.Background(), id)
		if err != nil {
			// Nếu lỗi liên quan đến không tìm thấy field thì trả về 404
			if strings.Contains(err.Error(), "field không tồn tại") {
				c.JSON(http.StatusNotFound, gin.H{"error": "Field không tồn tại"})
			} else {
				c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			}
			return
		}

		// Trả về dữ liệu dạng JSON
		c.JSON(http.StatusOK, gin.H{"data": field})
	}
}
