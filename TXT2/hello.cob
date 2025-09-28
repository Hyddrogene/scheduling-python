        IDENTIFICATION DIVISION.
        PROGRAM-ID. SumNumbers.

        ENVIRONMENT DIVISION.

        DATA DIVISION.
        WORKING-STORAGE SECTION.
        01 WS-TOTAL        PIC 9(4) VALUE 0.
        01 WS-COUNTER      PIC 9(2) VALUE 1.

        PROCEDURE DIVISION.
            DISPLAY 'Hello, World!'.

            PERFORM VARYING WS-COUNTER FROM 1 BY 1 UNTIL WS-COUNTER > 10
                ADD WS-COUNTER TO WS-TOTAL
            END-PERFORM
            DISPLAY 'Total sum is: ' WS-TOTAL
            STOP RUN.
