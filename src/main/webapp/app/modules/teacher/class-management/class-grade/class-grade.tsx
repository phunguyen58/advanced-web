import React, { useEffect, useRef, useState } from 'react';
import './class-grade.scss';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getSortState, translate } from 'react-jhipster';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { IUserCourse } from 'app/shared/model/user-course.model';
import { getAssignmentsByCourseId, getAssimentGrades, getCourse, getGradeBoards, getUserById, getUserCourses } from './grade-util';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { ICourse } from 'app/shared/model/course.model';
import { Button } from 'primereact/button';
import { IGradeBoard } from 'app/shared/model/grade-board.model';
import { getEntity } from 'app/entities/course/course-router/course.reducer';
import { GradeType } from 'app/shared/model/enumerations/grade-type.model';

const ClassGrade = () => {
  const dt = useRef<DataTable<IGradeBoard[]>>(null);
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const { id } = useParams();
  const [data, setData] = useState<IGradeBoard[]>([]);
  const course: ICourse = useAppSelector(state => state.course.entity);

  useEffect(() => {
    const fetch = async () => {
      const gradeboards = (await getGradeBoards(id)).data;
      setData(gradeboards);
      return gradeboards;
    };
    fetch().then(value => {
      console.log('value: ', value);
    });
  }, []);

  // Rest of your component code remains unchanged

  const exportCSV = () => {
    const exportedData = data.map(row => {
      const rowData = {
        [translate('webApp.classManagement.studentId')]: row.user.studentId,
      };

      // const assignmentGrade = rowData.userAssignmentGradesInCourse.find(value => value.assignment?.id === assignment?.id);
      data?.[0].assignmentsInCourse?.forEach((assignment, index) => {
        const assignmentGrade = row.userAssignmentGradesInCourse?.find(value => value.assignment?.id === assignment?.id);
        rowData[assignment.name] = assignmentGrade ? assignmentGrade.grade : null;
      });
      rowData[translate('webApp.classManagement.finalGrade')] = row.finalGrade ? String(row.finalGrade) : null;

      return rowData;
    });

    import('papaparse').then(papaparse => {
      const csv = papaparse.default.unparse(exportedData, { header: true });
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.setAttribute('download', 'grades.csv');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    });
  };

  const exportXLSX = () => {
    import('xlsx').then(xlsx => {
      const exportedData = data.map(row => {
        const rowData = {
          [translate('webApp.classManagement.studentId')]: row.user.studentId,
        };

        data?.[0].assignmentsInCourse.forEach((assignment, index) => {
          const assignmentGrade = row.userAssignmentGradesInCourse?.find(value => value.assignment?.id === assignment?.id);
          rowData[assignment.name] = assignmentGrade ? assignmentGrade.grade : null;
        });

        rowData[translate('webApp.classManagement.finalGrade')] = row.finalGrade ? String(row.finalGrade) : null;

        return rowData;
      });

      const ws = xlsx.utils.json_to_sheet(exportedData);
      const wb = xlsx.utils.book_new();
      xlsx.utils.book_append_sheet(wb, ws, 'Grades');
      xlsx.writeFile(wb, 'grades.xlsx');
    });
  };

  const header = (
    <div className=" aw-class-grade-container flex align-items-center justify-content-end gap-2">
      <Button className="aw-aw-operation-button" type="button" rounded={true} onClick={exportCSV} data-pr-tooltip="CSV">
        <img src="content/images/csv-export.png" alt="" />
      </Button>
      <Button
        className="aw-aw-operation-button"
        type="button"
        icon="pi pi-file-excel"
        severity="success"
        rounded={true}
        onClick={exportXLSX}
        data-pr-tooltip="XLS"
      />
    </div>
  );

  return (
    <div className="class-detail-container">
      <DataTable ref={dt} stripedRows={true} value={data} tableStyle={{ minWidth: '50rem' }} header={header}>
        <Column key={'studentId'} field="studentId" header={translate('webApp.classManagement.studentId')}></Column>
        {/* TODO: implement final grade */}
        {data.length > 0
          ? data[0].assignmentsInCourse?.map((assignment, index) => (
              <Column
                key={'studentId'}
                header={assignment.name}
                field={assignment.name}
                body={(rowData: IGradeBoard) => {
                  const assignmentGrade = rowData.userAssignmentGradesInCourse.find(value => value.assignment?.id === assignment?.id);
                  return assignmentGrade ? assignmentGrade.grade : null;
                }}
              ></Column>
            ))
          : null}
        <Column
          key={'studentId'}
          field="finalGrade"
          header={translate('webApp.classManagement.finalGrade')}
          body={(rowData: IGradeBoard) => {
            return rowData.gradeType === GradeType.PERCENTAGE ? Math.round(rowData.finalGrade * 100 * 100) / 100 + '%' : rowData.finalGrade;
          }}
        ></Column>
      </DataTable>
    </div>
  );
};

export default ClassGrade;
