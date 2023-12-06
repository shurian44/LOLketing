package com.ezen.lolketing.exception

sealed class BoardException(
    override val message: String?
) : Throwable() {
    object SearchError: BoardException("게시글 정보를 불러오는데 실패하였습니다")

    object TeamError: BoardException("팀 정보를 찾을 수 없습니다.")
}